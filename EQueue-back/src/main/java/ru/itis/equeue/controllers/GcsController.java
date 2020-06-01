package ru.itis.equeue.controllers;

        import java.io.IOException;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.MediaType;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.multipart.MultipartFile;
        import org.springframework.web.servlet.mvc.support.RedirectAttributes;
        import ru.itis.equeue.services.GcsStorageService;

@RestController
public class GcsController {

    @Autowired
    private GcsStorageService gcsStorageService;

    @RequestMapping(value = "/upload_result", method = RequestMethod.GET)
    public @ResponseBody
    String result(@RequestParam("result") String result) {
        return result;
    }

    @PostMapping("/upload_document")
    String uploadDocumentToGCS(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        String log = gcsStorageService.documentUpload(file);
        System.out.println(log);
        redirectAttributes.addFlashAttribute("message", log);
        return "redirect:/upload_result?result=" + log;
    }

    @PostMapping("/upload_avatar")
    String uploadAvatarToGCS(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        gcsStorageService.avatarUpload(file);
        return "redirect:/upload_result?result";
    }

    @PostMapping(value = "/upload_documents",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadDocumentToGCS(@RequestParam MultipartFile[] files) throws IOException {

        for (int i = 0; i < files.length; i++) {
            System.out.println(String.format("File name '%s' uploaded successfully. ", files[i].getOriginalFilename()));
        }

        String log = gcsStorageService.documentsUpload(files);
        System.out.println(log);

//        return "redirect:/";
//        ?result=" + log
        return "redirect:/upload_result?result=" + log;
    }

//    @GetMapping("/upload_result")
//    public String index() {
//        return "upload";
//    }

}