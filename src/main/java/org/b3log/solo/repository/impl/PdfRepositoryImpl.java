package org.b3log.solo.repository.impl;

import org.b3log.latke.repository.AbstractRepository;
import org.b3log.latke.repository.annotation.Repository;
import org.b3log.solo.model.Pdf;
import org.b3log.solo.repository.PdfRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: qijun
 * @email: 18353367683@163.com
 * @date: 2018/6/13 0013 18:29
 * @version: 1.1.0
 * @description:
 */
@Repository
public class PdfRepositoryImpl extends AbstractRepository implements PdfRepository {

    public PdfRepositoryImpl() {
        super(Pdf.PDF);
    }
}
