package com.minhduc.tuto.springboot.primefaces;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class ViewScope implements Scope {
    @Override
    public Object get(String name, ObjectFactory<? extends Object> objectFactory) {
	Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();
	Object obj;
	if (viewMap.containsKey(name)) {
	    obj = viewMap.get(name);
	} else {
	    Object object = objectFactory.getObject();
	    viewMap.put(name, object);
	    obj = object;
	}
	return obj;
    }

    @Override
    public Object remove(String name) {
	return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(name);
    }

    @Override
    public String getConversationId() {
	return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
	// Not supported
    }

    @Override
    public Object resolveContextualObject(String key) {
	return null;
    }
}
