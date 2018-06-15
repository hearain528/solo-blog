package org.b3log.solo.processor.console;

import jodd.upload.FileUpload;
import jodd.upload.MultipartStreamParser;
import jodd.upload.impl.MemoryFileUploadFactory;
import jodd.util.MimeTypes;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.event.Event;
import org.b3log.latke.event.EventException;
import org.b3log.latke.event.EventManager;
import org.b3log.latke.ioc.inject.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.Plugin;
import org.b3log.latke.plugin.ViewLoadEventData;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.JSONRenderer;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;
import org.b3log.latke.util.Strings;
import org.b3log.solo.model.Pdf;
import org.b3log.solo.processor.renderer.ConsoleRenderer;
import org.b3log.solo.service.PdfQueryService;
import org.b3log.solo.service.UserQueryService;
import org.b3log.solo.util.Solos;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * pdf预览数据
 *
 * @author qijun
 * @date 2018/6/13 0013 17:55
 */
@RequestProcessor
public class PDFViewConsole {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PDFViewConsole.class);

    @Inject
    private UserQueryService userQueryService;

    @Inject
    private PdfQueryService pdfQueryService;

    /**
     * Event manager.
     */
    @Inject
    private EventManager eventManager;

    /**
     * pdf预览首页
     * @author qijun
     * @date 2018/6/14 0014 10:33
     * @param
     * @return
     * @throws
     */
    @RequestProcessing(value = "/pdf-index.do", method = HTTPRequestMethod.GET)
    public void showPdfIndex(final HttpServletRequest request, final HttpServletResponse response, final HTTPRequestContext context) throws Exception{
        if (!userQueryService.isAdminLoggedIn(request)) {
            response.sendRedirect("/login");
            return;
        }
        final AbstractFreeMarkerRenderer renderer = new ConsoleRenderer();

        context.setRenderer(renderer);
        final String templateName = "pdf-index.ftl";

        renderer.setTemplateName(templateName);

        final Map<String, Object> dataModel = renderer.getDataModel();

        fireFreeMarkerActionEvent(templateName, dataModel);
    }

    /**
     * pdf上传展示页
     * @author qijun
     * @date 2018/6/14 0014 10:33
     * @param
     * @return
     * @throws
     */
    @RequestProcessing(value = "/pdf-upload.do", method = HTTPRequestMethod.GET)
    public void showPdfUpload(final HttpServletRequest request, final HttpServletResponse response, final HTTPRequestContext context) throws Exception{
        if (!userQueryService.isAdminLoggedIn(request)) {
            response.sendRedirect("/login");
            return;
        }
        final AbstractFreeMarkerRenderer renderer = new ConsoleRenderer();

        context.setRenderer(renderer);
        final String templateName = "pdf-upload.ftl";

        renderer.setTemplateName(templateName);

        final Map<String, Object> dataModel = renderer.getDataModel();

        fireFreeMarkerActionEvent(templateName, dataModel);
    }

	/**
	 * pdf上传展示页
	 * @author qijun
	 * @date 2018/6/14 0014 10:33
	 * @param
	 * @return
	 * @throws
	 */
	@RequestProcessing(value = "/pdf-viewer.do", method = HTTPRequestMethod.GET)
	public void showPdfViewer(final HttpServletRequest request, final HttpServletResponse response, final HTTPRequestContext context) throws Exception{
		if (!userQueryService.isAdminLoggedIn(request)) {
			response.sendRedirect("/login");
			return;
		}
		final AbstractFreeMarkerRenderer renderer = new ConsoleRenderer();

		context.setRenderer(renderer);
		final String templateName = "pdf-viewer.ftl";

		renderer.setTemplateName(templateName);

		final Map<String, Object> dataModel = renderer.getDataModel();
		String id = request.getParameter("id");
		JSONObject pdfinfo = pdfQueryService.getPdfInfo(id);
		dataModel.put("currentPage", pdfinfo.getInt("currentPage"));
		dataModel.put("url", Solos.UPLOAD_DIR_PATH + pdfinfo.getString("idname") + ".pdf");
		dataModel.put("id", id);

		fireFreeMarkerActionEvent(templateName, dataModel);
	}


    @RequestProcessing(value = "/file_pdf_upload.do", method = HTTPRequestMethod.POST)
    public void actionPdfUpload(final HttpServletRequest request, final HttpServletResponse response, final HTTPRequestContext context) throws Exception{
        final int maxSize = 1024 * 1024 * 100;
        final MultipartStreamParser parser = new MultipartStreamParser(new MemoryFileUploadFactory().setMaxFileSize(maxSize));
        parser.parseRequestStream(request.getInputStream(), "UTF-8");
        final FileUpload[] files = parser.getFiles("pdf");
        String fileName;

        for (int i = 0; i < files.length; i++) {
            final FileUpload file = files[i];
            final String originalName = fileName = file.getHeader().getFileName();
            try {
                String suffix = StringUtils.substringAfterLast(fileName, ".");
                final String contentType = file.getHeader().getContentType();
                if (StringUtils.isBlank(suffix)) {
                    String[] exts = MimeTypes.findExtensionsByMimeTypes(contentType, false);
                    if (null != exts && 0 < exts.length) {
                        suffix = exts[0];
                    } else {
                        suffix = StringUtils.substringAfter(contentType, "/");
                    }
                }

                final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                fileName = uuid + "." + suffix;

                final OutputStream output = new FileOutputStream(Solos.UPLOAD_DIR_PATH + fileName);
                IOUtils.copy(file.getFileInputStream(), output);
                IOUtils.closeQuietly(file.getFileInputStream());
                IOUtils.closeQuietly(output);


                //添加数据到数据库
                JSONObject pdfinfo = new JSONObject();
                pdfinfo.put(Pdf.PDF_IDNAME, uuid);
                pdfinfo.put(Pdf.PDF_CURRNETPAGE, 1);
                pdfinfo.put(Pdf.PDF_REALNAME, originalName);
                pdfinfo.put(Pdf.PDF_SIZE, file.getSize());
                pdfQueryService.addPdf(pdfinfo);
            } catch (final Exception e) {
                LOGGER.log(Level.WARN, "Uploads file failed", e);
            }
        }
        response.sendRedirect("/pdf-index.do");
    }

    @RequestProcessing(value = "/file_pdf_delete.do", method = HTTPRequestMethod.GET)
    public void delete(final HttpServletRequest request, final HttpServletResponse response, final HTTPRequestContext context) throws Exception{
    	//删除数据
	    String id = request.getParameter("id");
	    JSONObject pdfinfo = pdfQueryService.getPdfInfo(id);
		pdfQueryService.deletePdf(id);
	    File file = new File(Solos.UPLOAD_DIR_PATH + pdfinfo.getString("idname") + ".pdf");
	    if(file.exists()){
	    	file.delete();
	    }else{
	    	throw new Exception("文件不存在");
	    }
	    response.sendRedirect("/pdf-index.do");
    }

    @RequestProcessing(value = "/console/pdfview/index", method = HTTPRequestMethod.GET)
    public void pdfViewIndex(final HttpServletRequest request, final HttpServletResponse response, final HTTPRequestContext context)
            throws Exception {
        if (!userQueryService.isAdminLoggedIn(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> pdfList = pdfQueryService.getPdfList();
        jsonObject.put("data", pdfList);
        renderer.setJSONObject(jsonObject);
    }

	@RequestProcessing(value = "/saveHistoryData.do", method = HTTPRequestMethod.POST)
	public void pdfsaveHistoryData(final HttpServletRequest request, final HttpServletResponse response, final HTTPRequestContext context)
			throws Exception {
		String page = request.getParameter("data");
		String pdfid = request.getParameter("id");
		final JSONRenderer renderer = new JSONRenderer();
		context.setRenderer(renderer);
		JSONObject jsonObject = new JSONObject();
		pdfQueryService.updatePdfInfo(pdfid, Integer.parseInt(page));
		jsonObject.put("success", true);
		renderer.setJSONObject(jsonObject);
	}

    /**
     * Fires FreeMarker action event with the host template name and data model.
     *
     * @param hostTemplateName the specified host template name
     * @param dataModel        the specified data model
     */
    private void fireFreeMarkerActionEvent(final String hostTemplateName, final Map<String, Object> dataModel) {
        try {
            final ViewLoadEventData data = new ViewLoadEventData();

            data.setViewName(hostTemplateName);
            data.setDataModel(dataModel);
            eventManager.fireEventSynchronously(new Event<ViewLoadEventData>(Keys.FREEMARKER_ACTION, data));
            if (Strings.isEmptyOrNull((String) dataModel.get(Plugin.PLUGINS))) {
                // There is no plugin for this template, fill ${plugins} with blank.
                dataModel.put(Plugin.PLUGINS, "");
            }
        } catch (final EventException e) {
            LOGGER.log(Level.WARN, "Event[FREEMARKER_ACTION] handle failed, ignores this exception for kernel health", e);
        }
    }

}
