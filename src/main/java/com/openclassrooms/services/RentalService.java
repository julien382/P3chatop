package com.openclassrooms.services;

import com.openclassrooms.dto.RentalDTO;
import com.openclassrooms.dto.RentalRequestDTO;
import com.openclassrooms.entity.Rental;
import com.openclassrooms.entity.User;
import com.openclassrooms.repository.RentalRepository;
import com.openclassrooms.repository.UserRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;


@Data
@Service
public class RentalService {
    @Value("${server.url}")
    private String serverUrl;
    private final RentalRepository rentalRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public RentalService(RentalRepository rentalRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    // Méthode pour obtenir un objet RentalDTO à partir de son ID
    public RentalDTO getRental(final Long id) {
        Optional<Rental> rental = rentalRepository.findById(id);
        return rental.map(r -> {
            RentalDTO rentalDTO = modelMapper.map(r, RentalDTO.class);
            rentalDTO.setOwner_id(r.getOwner().getId());
            rentalDTO.setPicture(r.getPicture());
            rentalDTO.setCreated_at(r.getCreated_at());
            rentalDTO.setUpdated_at(r.getUpdated_at());
            return rentalDTO;
        }).orElse(null);
    }

    // Méthode pour obtenir tous les objets RentalDTO
   public Map<String, List<RentalDTO>> getRentals() {
       List<Rental> rentals = rentalRepository.findAll();
       List<RentalDTO> rentalDTOs = rentals.stream()
               .map(rental -> {
                   RentalDTO rentalDTO = modelMapper.map(rental, RentalDTO.class);
                   rentalDTO.setOwner_id(rental.getOwner().getId());
                   rentalDTO.setPicture(rental.getPicture());
                   return rentalDTO;
               })
               .toList();
       return Collections.singletonMap("rentals", rentalDTOs);
   }

    // Méthode pour créer un nouvel objet Rental
    public void createRental(RentalRequestDTO rentalRequestDTO, Principal principal) {
        try {
            String email = principal.getName();
            User owner = userRepository.findAll().stream()
                    .filter(user -> user.getEmail().equals(email))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("User not found with email : " + email));
            rentalRequestDTO.setOwner_id(owner.getId());

            // Sauvegarde de l'image
            MultipartFile picture = rentalRequestDTO.getPicture();
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename()));
            Path path = Paths.get("src/main/resources/static/images/" + fileName);
            Files.copy(picture.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Génération de l'URL absolue de l'image
            String imageUrl = serverUrl + "/" + fileName;

            // Création d'un objet RentalDTO à partir de RentalRequestDTO
            RentalDTO rentalDTO = new RentalDTO();
            rentalDTO.setId(rentalRequestDTO.getId());
            rentalDTO.setName(rentalRequestDTO.getName());
            rentalDTO.setSurface(rentalRequestDTO.getSurface());
            rentalDTO.setPrice(rentalRequestDTO.getPrice());
            rentalDTO.setPicture(imageUrl); // Définition de l'URL de l'image
            rentalDTO.setDescription(rentalRequestDTO.getDescription());
            rentalDTO.setOwner_id(owner.getId());

            Rental rental = modelMapper.map(rentalDTO, Rental.class);
            rental.setOwner(owner);
            rental.setPicture(rentalDTO.getPicture());
            Rental savedRental = rentalRepository.save(rental);
            RentalDTO savedRentalDTO = modelMapper.map(savedRental, RentalDTO.class);
            savedRentalDTO.setOwner_id(savedRental.getOwner().getId());
            savedRentalDTO.setId(savedRental.getId()); // Définition de l'ID dans RentalDTO
        } catch (Exception e) {
            throw new RuntimeException("Error while creating rental: " + e.getMessage());
        }
    }

    // Méthode pour mettre à jour un objet Rental
    public RentalDTO updateRental(Long id, RentalDTO rentalDTO) {
        Optional<Rental> rentalOpt = rentalRepository.findById(id);
        if (rentalOpt.isPresent()) {
            Rental rental = rentalOpt.get();
            LocalDateTime originalCreatedAt = rental.getCreated_at(); // Sauvegarde du created_at original
            rentalDTO.setOwner_id(rental.getOwner().getId());
            rentalDTO.setPicture(rental.getPicture()); // Conservation du chemin de l'image
            Long originalId = rental.getId(); // Sauvegarde de l'ID original
            modelMapper.map(rentalDTO, rental);
            rental.setId(originalId); // Rétablir l'ID original
            rental.setCreated_at(originalCreatedAt); // Rétablir la valeur du created_at original
            rental.setUpdated_at(LocalDateTime.now());
            Rental savedRental = rentalRepository.save(rental);
            return modelMapper.map(savedRental, RentalDTO.class);
        } else {
            return null;
        }
    }
}
