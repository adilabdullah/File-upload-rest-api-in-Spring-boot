package com.file.upload.controller;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.file.upload.model.Response;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

public class FileController {
	

	  @PostMapping("/upload") 
	  @ResponseBody
	  public Response handleFileUpload(@RequestParam("name") String name,@RequestParam("emp_no") String emp_no,@RequestParam Map<String,MultipartFile> files) {
		  String ext="";
	//	  return "Success";
		    String path="D:\\NodeJs\\";
		  Date date = new Date();
	      SimpleDateFormat ft=new SimpleDateFormat ("dd/MM/yyyy-hh:mm:ss");
		  
          System.out.println(name);
          System.out.println(emp_no);
	    try {
	    	path=path+name+"_"+emp_no;
	    	System.out.println(path);
	    	File f1 = new File(path);  
	         f1.mkdir();
	        
	        	   
	        	  Iterator <String> it = files.keySet().iterator();       //keyset is a method  
	    		  while(it.hasNext())  
	    		  {  
	    		   String key=it.next();  
                   ext=FileController.getExtension(files.get(key).getOriginalFilename()).toUpperCase();
	    		   if(!ext.contains("PDF") && !ext.contains("DOCX") && !ext.contains("DOC"))
	    		   {       
	    			   FileUtils.deleteDirectory(new File(path));
	    			   return new Response("File format not correct "+files.get(key).getOriginalFilename());  
                       
	    		   }
	    		   else
	    		   {
	    			   if(files.get(key).getSize()/1024>2048)
	    			   {
	    			       FileUtils.deleteDirectory(new File(path));
	    				   return new Response("File size exceeded "+files.get(key).getOriginalFilename());
	    			   }
	    			   else
	    			   {
	    				   files.get(key).transferTo(new File(path+"\\"+key.toUpperCase()+"."+FileController.getExtension(files.get(key).getOriginalFilename())));   
	    			   }
	    		   }
	    	
	    		  }  
	    		  return new Response("Success");
	    		//  return "Success";
	          
	         
	    } catch (Exception e) {
	      return new Response(e.getMessage());
	    }   
	   
	  }
	  
	  
	  
	  @GetMapping("/fileList/{name}/{id}") 
	  public Map<String,String> getAllFiles(@PathVariable("name") String name,@PathVariable ("id") int id)
	  { 
		 // List<String> li=new ArrayList<String>();
		  Map<String,String> mp=new HashMap<String,String>();
		  String[] pathnames;

      // Creates a new File instance by converting the given pathname string
      // into an abstract pathname
      File f = new File("D:\\NodeJs\\"+name+"_"+id);

      // Populates the array with names of files and directories
      pathnames = f.list();

      // For each pathname in the pathnames array
      for (String pathname : pathnames) {
          // Print the names of files and directories
        //  li.add(pathname);
    	  mp.put(pathname.substring(0,pathname.indexOf('.')), pathname);
      }
      return mp;
  }
	  
	  
	  public static String getExtension(String filename) {
		    return FilenameUtils.getExtension(filename);
		}
	  

	
}
