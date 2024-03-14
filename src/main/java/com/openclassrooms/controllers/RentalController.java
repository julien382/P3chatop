package com.openclassrooms.controllers;

import com.openclassrooms.dto.RentalDTO;
import com.openclassrooms.dto.RentalRequestDTO;
import com.openclassrooms.services.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // Endpoint pour créer une nouvelle location
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createRental(@ModelAttribute RentalRequestDTO rentalRequestDTO, Principal principal) {
        try {
            // Appel du service pour créer la location
            rentalService.createRental(rentalRequestDTO, principal);
            // Réponse indiquant que la location a été créée avec succès
            return new ResponseEntity<>(Collections.singletonMap("message", "Rental created !"), HttpStatus.CREATED);
        } catch (Exception e) {
            // Réponse en cas d'erreur lors de la création de la location
            return new ResponseEntity<>(Collections.singletonMap("error", "Error while creating rental: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint pour obtenir une location par son ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RentalDTO getRental(@PathVariable("id") final Long id) {
        // Appel du service pour récupérer la location par son ID
        return rentalService.getRental(id);
    }

    // Endpoint pour obtenir toutes les locations
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, List<RentalDTO>> getRentals() {
        // Appel du service pour récupérer toutes les locations
        return rentalService.getRentals();
    }

    // Endpoint pour mettre à jour une location par son ID
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateRental(@PathVariable("id") final Long id, @ModelAttribute RentalDTO rentalDTO) {
        try {
            // Appel du service pour mettre à jour la location
            RentalDTO updatedRental = rentalService.updateRental(id, rentalDTO);
            if (updatedRental != null) {
                // Réponse indiquant que la location a été mise à jour avec succès
                return new ResponseEntity<>(Collections.singletonMap("message", "Rental updated !"), HttpStatus.OK);
            } else {
                // Réponse en cas de location non trouvée
                return new ResponseEntity<>(Collections.singletonMap("error", "Rental not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Réponse en cas d'erreur lors de la mise à jour de la location
            return new ResponseEntity<>(Collections.singletonMap("error", "Error while updating rental: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}