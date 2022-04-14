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
import java.io.FileInputStream;

import java.io.IOException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.zeroturnaround.zip.ZipUtil;


@Controller
public class ZipController {

    private static final Logger logger = LoggerFactory.getLogger(ZipController.class);

    @GetMapping("/laminar/zip/{serviceName}")
    public ResponseEntity<Resource> getZip(@PathVariable String serviceName) {
        String zipName = serviceName + ".zip";
        File zipFile = new File(zipName);
        InputStreamResource resource = null;
        File generatedCodeFile = new File(serviceName + "/");
        ZipUtil.pack(generatedCodeFile, zipFile);
        try {
            resource = new InputStreamResource(new FileInputStream(zipFile));
        } catch (Exception e){
            logger.error("Error in zipping - " + e);
        }

        try{
            FileUtils.deleteDirectory(generatedCodeFile);
//            zipFile.delete();
        }

        catch (IOException e){
            logger.error("error while deleting the generated folder",e);
        }

        ResponseEntity<Resource> response = ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipName + "\"")
            .contentLength(zipFile.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);

        boolean status = zipFile.delete();
        logger.info("Status of zip file deletion = {}",status);
        return response;

    }
}
