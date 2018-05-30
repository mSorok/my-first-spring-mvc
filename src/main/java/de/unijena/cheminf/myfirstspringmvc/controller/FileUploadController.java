package de.unijena.cheminf.myfirstspringmvc.controller;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.stream.Collectors;
import java.io.File;

import de.unijena.cheminf.myfirstspringmvc.nplikeness.NPWorker;
import org.openscience.cdk.interfaces.IAtomContainer;
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

import org.springframework.validation.BindingResult;


import de.unijena.cheminf.myfirstspringmvc.storage.StorageFileNotFoundException;
import de.unijena.cheminf.myfirstspringmvc.storage.StorageService;


import de.unijena.cheminf.myfirstspringmvc.nplikeness.misc.InputParser;

@Controller
public class FileUploadController {


    private final StorageService storageService;
    private boolean returnedResults = false;

    public ArrayList<String> moleculesWithScores = new ArrayList<String>();

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));



        return "index";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /*@PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }*/


   @PostMapping("/")
    public String launchNPScorerOnFile(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if(!file.isEmpty()) {
            storageService.store(file);
            redirectAttributes.addFlashAttribute("uploadSuccessful",
                    "Working on scoring file! " + file.getOriginalFilename() + "!");


            String loadedFile = "upload-dir/" + file.getOriginalFilename();

            NPWorker worker = new NPWorker(loadedFile);

            redirectAttributes.addFlashAttribute("workerStarted",
                    "NP-likeness scorer started");



            worker.controlMolecules();
            System.out.println(worker.getMoleculesWithScores());
            ArrayList<String> newMoleculesToString = worker.getMoleculeString();

            this.moleculesWithScores.addAll(newMoleculesToString);


            return "redirect:/results";//redirect to results page when finished
        }
        else{

            redirectAttributes.addFlashAttribute("noFileError",
                    "You need to load a valid SDF file!");
            return "redirect:/";

        }
    }


    @GetMapping("results")
    public String listScores(Model model) throws IOException {


        model.addAttribute("scores", this.moleculesWithScores);
        System.out.println(this.moleculesWithScores);

        return "results";
    }

    /*@GetMapping("/wait")
    public String letsWait(RedirectAttributes redirectAttributes){

        System.out.println("In wait 1");

       System.out.println("\n\n WAITING \n\n\n");
       // redirectAttributes.addFlashAttribute("wait",
         //       "Waiting for the results!");

       //DO STUFF HERE
        System.out.println("In wait");

       return "redirect:/results";
    }
*/
   /* @RequestMapping("/results")
    @ResponseBody
    public String postResults(RedirectAttributes redirectAttributes){
            System.out.println("\n\n Results \n\n\n");
            redirectAttributes.addFlashAttribute("result",
                    "Here will be results");
            System.out.println("In results");
            return "redirect:/results";

    }

*/

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    //Cast multipart spring file to conventional file
    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException
    {
        File convFile = new File( multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }
}
