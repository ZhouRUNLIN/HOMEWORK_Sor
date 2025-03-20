package org.rhw.bmr.project.service;

import org.rhw.bmr.project.dto.req.TextInternalSearchByEgreplikeReqDTO;
import org.rhw.bmr.project.dto.req.TextInternalSearchByKMPReqDTO;

public interface TextInternalSearchService {

    String[] TextInternalSearchByKMP(TextInternalSearchByKMPReqDTO requestParam);

    String[] TextInternalSearchByEgreplike(TextInternalSearchByEgreplikeReqDTO requestParam);


    long[] TextInternalSearchByKMPLong(TextInternalSearchByKMPReqDTO requestParam);

    long[] TextInternalSearchByEgreplikeLong(TextInternalSearchByEgreplikeReqDTO requestParam);

}
