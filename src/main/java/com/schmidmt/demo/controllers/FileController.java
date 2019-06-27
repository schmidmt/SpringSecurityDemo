package com.schmidmt.demo.controllers;

import com.schmidmt.demo.models.UploadedFile;
import com.schmidmt.demo.models.dao.UploadedFileRepository;
import com.schmidmt.demo.services.storage.StorageException;
import com.schmidmt.demo.services.storage.StorageFileNotFoundException;
import com.schmidmt.demo.services.storage.StorageService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("file")
public class FileController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    public FileController() { }

    @GetMapping("")
    public String listUploadedFiles(Model model) throws IOException {

        Iterable<UploadedFile> files = uploadedFileRepository.findAll();

        List<String> attrFiles = StreamSupport.stream(files.spliterator(), false)
            .map(file -> MvcUriComponentsBuilder
                .fromMethodName(FileController.class, "serveFile", file.path(storageService).getFileName().toString()).build().toString()
            ).collect(Collectors.toList());

        model.addAttribute("files", attrFiles);

        return "files/list";
    }

    @GetMapping("{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        Tika tika = new Tika();
        String fileType;
        try {
            fileType = tika.detect(file.getFile());
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity
                .ok()
                .header("Content-Type", fileType)
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        UploadedFile uf = new UploadedFile();

        uf.update(storageService, file);
        Tika tika = new Tika();
        String fileType;
        try {
            fileType = tika.detect(file.getBytes());
        } catch (IOException e) {
            throw new StorageException("Cannot determine filetype", e);
        }

        uf.setName(file.getOriginalFilename());
        uf.setMimeType(fileType);

        uploadedFileRepository.save(uf);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/file";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }


    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageFileNotFoundException exc) {
        return ResponseEntity.badRequest().build();
    }

}
