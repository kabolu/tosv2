package edu.must.tos.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.log4j.Logger;

public class UploadFile {

	public static List upload(HttpServletRequest request, int size) {
		FileItem fileItem = null;
		List elements = new ArrayList();
		String sizeInfo = "right";
		try {
			RequestContext requestContext = new ServletRequestContext(request);
			if (ServletFileUpload.isMultipartContent(requestContext)) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1024 * 1024);
				ServletFileUpload upload = new ServletFileUpload(factory);

				upload.setFileSizeMax(size * 1024 * 1024);

				List items = null;
				try {
					items = upload.parseRequest(requestContext);
				} catch (FileUploadException e) {
					e.printStackTrace();
					if (e instanceof FileSizeLimitExceededException) {
						sizeInfo = "wrong";
					}
					Logger log = Logger.getLogger("edu.must.tos.util.upload");
					log.info(e.getMessage());
				}
				List elementList = new ArrayList();
				if (items != null) {
					for (int i = 0; i < items.size(); i++) {
						FileItem fi = (FileItem) items.get(i);
						if (!fi.isFormField()) {
							//String fileName = fi.getName().substring(fi.getName().lastIndexOf("\\") + 1,fi.getName().length());
							
							//String extName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
							
							fileItem = fi;
							// break;
						} else {
							//String field = fi.getFieldName();
							
							String element = fi.getString();
							
							elementList.add(element);
						}
					}
				}
				elements.add(fileItem);
				elements.add(elementList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		elements.add(sizeInfo);
		return elements;
	}
}
