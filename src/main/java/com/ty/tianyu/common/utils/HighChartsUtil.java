package com.ty.tianyu.common.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.svg.PDFTranscoder;

/**
 * @description highCharts导出图片
 * @author ty
 * @date 2014年4月4日 下午3:51:32
 */
@WebServlet(urlPatterns="/HighChartsUtil")
public class HighChartsUtil extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HighChartsUtil() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response); 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");//设置编码，解决乱码问题
		String type = request.getParameter("type");
		String svg = request.getParameter("svg");
		String filename = request.getParameter("filename");
		filename = filename==null?"chart":filename;
		ServletOutputStream out = response.getOutputStream();
		if (null != type && null != svg) {
		    svg = svg.replaceAll(":rect", "rect");
		    String ext = "";
		    Transcoder t = null;
		    if (type.equals("image/png")) {
				ext = "png";
				t = new PNGTranscoder();
		    } else if (type.equals("image/jpeg")) {
				ext = "jpg";
				t = new JPEGTranscoder();
		    } else if (type.equals("application/pdf")) {
                ext = "pdf";
                t =(Transcoder) new PDFTranscoder();
	        } else if(type.equals("image/svg+xml")) 
	            ext = "svg";   
		    response.addHeader("Content-Disposition", "attachment; filename="+ filename + "."+ext);
		    response.addHeader("Content-Type", type);
		    
		    if (null != t) {
				TranscoderInput input = new TranscoderInput(new StringReader(svg));
				TranscoderOutput output = new TranscoderOutput(out);
				
				try {
				    t.transcode(input, output);
				} catch (TranscoderException e) {
				    out.print("Problem transcoding stream. See the web logs for more details.");
				    e.printStackTrace();
				}
		    } else if (ext.equals("svg")) {
		    //	out.print(svg);
		    	OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
		    	writer.append(svg);
		    	writer.close();
		    } else 
		    	out.print("Invalid type: " + type);
		} else {
		    response.addHeader("Content-Type", "text/html");
		    out.println("Usage:\n\tParameter [svg]: The DOM Element to be converted." +
		    		"\n\tParameter [type]: The destination MIME type for the elment to be transcoded.");
		}
		out.flush();
		out.close();
	}

}
