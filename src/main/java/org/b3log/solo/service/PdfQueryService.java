package org.b3log.solo.service;

import org.b3log.latke.Keys;
import org.b3log.latke.ioc.inject.Inject;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.Transaction;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.CollectionUtils;
import org.b3log.latke.util.Ids;
import org.b3log.solo.repository.PdfRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: qijun
 * @email: 18353367683@163.com
 * @date: 2018/6/13 0013 18:46
 * @version: 1.1.0
 * @description:
 */
@Service
public class PdfQueryService {

    @Inject
    private PdfRepository pdfRepository;

    public List<JSONObject> getPdfList() throws RepositoryException, JSONException {
        final JSONObject result = pdfRepository.get(new Query());
        final JSONArray pdflist = result.getJSONArray(Keys.RESULTS);
        return CollectionUtils.jsonArrayToList(pdflist);
    }

    /**
     * 添加pdf数据
     * @author qijun
     * @date 2018/6/14 0014 11:18
     * @param
     * @return
     * @throws
     */
    public void addPdf(JSONObject pdfinfo) throws Exception{
        pdfinfo.put(Keys.OBJECT_ID, Ids.genTimeMillisId());
        final Transaction transaction = pdfRepository.beginTransaction();
        pdfRepository.add(pdfinfo);
        transaction.commit();
    }
}
