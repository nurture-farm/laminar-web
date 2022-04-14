/*
 *  Copyright 2022 Nurture.Farm
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package farm.nurture.laminarweb.controller;

import farm.nurture.infra.util.Logger;
import farm.nurture.infra.util.LoggerFactory;
import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import farm.nurture.laminar.generator.ProtoGenerator;

@Controller
public class FileUploadController {

    private final String devBasePath = "/laminar-web/";
    @Value("${localPath}")
    private String path;
    @Value("${dbUrl}")
    private String dbUrl;
    @Value("${username}")
    private String userName;
    @Value("${password}")
    private String password;
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @PostMapping("/laminar/uploadFile")
    public String handleFileUpload2(@RequestParam("configFile") MultipartFile file1, @RequestParam("dumpFile") MultipartFile file2,
                                    @RequestParam("lamFile") MultipartFile file3, RedirectAttributes redirectAttributes) {

        if(file1.isEmpty() || file2.isEmpty() || file3.isEmpty()){
            logger.error("Uploaded files are empty");
            return "redirect:/laminar/error/" +"input files are empty";
        }
        String uuid = "lam_" + UUID.randomUUID().toString().substring(0, 8);
        uuid = uuid.replace('-','_');

        logger.info("Generating laminar code for - " + uuid + " at " + System.currentTimeMillis());
        logger.info("Path "+path);
        String configFilePath = path + "config_" + uuid + ".json";
        String dumpFilePath = path + "dump_" + uuid + ".sql";
        String lamFilePath = path + "application_" + uuid + ".lam";

        File configFile = new File(configFilePath);
        File dumpFile = new File(dumpFilePath);
        File lamFile = new File(lamFilePath);

        String serviceName;

        try {
            file1.transferTo(configFile);
            file2.transferTo(dumpFile);
            file3.transferTo(lamFile);

            ProtoGenerator protoGenerator = new ProtoGenerator();
            serviceName = protoGenerator.generateCode(new String [] {"go,proto,graphql",
                configFilePath, dumpFilePath, lamFilePath, uuid, userName, password, dbUrl, path} );
        } catch (Exception e){

            logger.error("Error in upload file for uuid : " + uuid + " is - " + e);
            return "redirect:/laminar/error/" + e.getMessage();

        } finally {
            configFile.delete();
            dumpFile.delete();
            lamFile.delete();
        }
        redirectAttributes.addFlashAttribute("message", "Files uploaded successfully. Please wait until the download starts.");
        return "redirect:/laminar/zip/" + serviceName;
    }

    @PostMapping("/laminar/error/{exceptionMessage}")
    public String test(RedirectAttributes redirectAttributes, @PathVariable String exceptionMessage) {
        redirectAttributes.addFlashAttribute("message", "Uploaded files have some error. Please fix and re-upload.");
        return "error";
    }

}