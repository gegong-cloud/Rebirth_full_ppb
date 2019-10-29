package com.google.android.mvp.model.back_entity;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * 首页云播标题组合数据
 */
public class CloudSection extends SectionEntity<LabelEntity.LabelList> {

    private String id;
    public CloudSection(boolean isHeader, String header,String id) {
        super(isHeader, header);
        this.id = id;
    }

    public CloudSection(LabelEntity.LabelList labelList) {
        super(labelList);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
