/**
 * 
 */
package com.mysite.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.wcm.core.components.models.Teaser;
import com.mysite.core.models.MySiteTeaserModel;

/**
 * @author aasishjain
 *
 */
@Model(
        adaptables = { Resource.class},
        adapters = { Teaser.class, MySiteTeaserModel.class},
        resourceType = { MySiteTeaserModelImpl.TEASER_RESOURCE_TYPE },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MySiteTeaserModelImpl implements MySiteTeaserModel {
	
	protected static final String TEASER_RESOURCE_TYPE = "mysite/components/content/mysiteteaser";
	
	@Self
    @Via(type = ResourceSuperType.class)
    private Teaser teaser;
	
	@Override
    public String getTitle() {
        return "Aasish"+teaser.getTitle();
    }

}
