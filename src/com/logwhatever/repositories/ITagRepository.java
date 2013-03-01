package com.logwhatever.repositories;

import com.logwhatever.models.Session;
import com.logwhatever.models.Tag;
import com.logwhatever.service.IExecutor;
import java.util.List;

public interface ITagRepository extends IRepository<Tag> {
    void latest(Session session, IExecutor<List<Tag>> callback);
    void create(Session session, Tag tag);
}
