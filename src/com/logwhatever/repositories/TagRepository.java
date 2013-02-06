package com.logwhatever.repositories;

import com.logwhatever.models.Session;
import com.logwhatever.models.Tag;
import com.logwhatever.service.IExecutor;
import java.util.List;

public class TagRepository extends BaseRepository<Tag> implements ITagRepository {
    public String getCollection() { return "tags"; }
    public Class<Tag> getType() { return Tag.class; }
    
    public void latest(Session session, final IExecutor<List<Tag>> callback) {
	all(session, new IExecutor<List<Tag>>() {
	    public void execute(List<Tag> parameter) {
		callback.execute(parameter);
	    }
	});
    }

}
