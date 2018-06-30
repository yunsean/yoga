package com.yoga.content.property.cache;

import com.yoga.content.property.model.Property;
import com.yoga.content.property.repo.PropertyRepository;
import com.yoga.core.cache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class PropertyCache extends BaseCache {

	@Autowired
	private PropertyRepository propertyRepository = null;

	@Cacheable(value = "propertyMap", keyGenerator = "wiselyKeyGenerator")
	public Map<String, Property> propertyMap(long tenantId) {
		List<Property> properties = propertyRepository.findByTenantId(tenantId);
		Map<Long, Property> mapOptions = new HashMap<>();
		for (Property optionen : properties) {
			mapOptions.put(optionen.getId(), optionen);
		}
		Iterator<Entry<Long, Property>> it = mapOptions.entrySet().iterator();
		Map<String, Property> result = new HashMap<>();
		while(it.hasNext()) {
			Property self = it.next().getValue();
			Property parent = mapOptions.get(self.getParentId());
		   if (parent != null) {
			   self.setValue(parent.getValue() + self.getName());
			   parent.addChild(self);
		   } else {
			   result.put(self.getCode(), self);
		   }
		}	
		return result;
	}

	public void cleanCache(long tenantId) {
		redis.remove("com.yoga.content.property.cache.PropertyCache.propertyMap." + tenantId);
	}
}
