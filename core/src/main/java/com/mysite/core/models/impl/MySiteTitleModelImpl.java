/**
 * 
 */
package com.mysite.core.models.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.models.Title;
import com.mysite.core.models.MySiteTitleModel;

/**
 * 
 * Using sling delegation in extending the core title component.
 * In order to use sling delegation for sling model we must use SlingHttpServletRequest as adaptable.
 * @author aasishjain
 *
 */
@Model(
        adaptables = { Resource.class, SlingHttpServletRequest.class },
        adapters = { MySiteTitleModel.class },
        resourceType = { MySiteTeaserModelImpl.TEASER_RESOURCE_TYPE },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MySiteTitleModelImpl implements MySiteTitleModel {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	protected static final String TEASER_RESOURCE_TYPE ="mysite/components/content/mysitetitle";
	
	@Self
	@Via(type=ResourceSuperType.class)
	private Title model;
	
	@ValueMapValue
	private String description;
	
	@Override
	public String getText() {
		return "MySite"+model.getText();
	}

	@Override
	public String getDescription() {
		return description;
	}

}
