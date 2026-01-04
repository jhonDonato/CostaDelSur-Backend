package com.costadelsur.api.controller;

import com.costadelsur.api.service.impl.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El archivo está vacío"));
            }

            // Validar que sea una imagen
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Solo se permiten archivos de imagen");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Subir a Cloudinary
            String url = cloudinaryService.uploadFile(file);

            // Responder con la URL (Formato JSON esperado por el frontend)
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("url", url);

            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al subir la imagen: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/receipt")
    public ResponseEntity<?> uploadReceipt(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El archivo está vacío"));
            }

            // Validar tipo (Imagen o PDF)
            String contentType = file.getContentType();
            if (contentType == null ||
                    (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Solo se permiten imágenes o PDF");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Subir a Cloudinary
            String url = cloudinaryService.uploadFile(file);

            // Responder con metadatos
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("url", url);
            successResponse.put("fileName", file.getOriginalFilename());
            successResponse.put("fileType", contentType);

            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al subir el comprobante: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}