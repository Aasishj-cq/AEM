package com.mysite.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
Steps to Create a workflow model
1. Go to AEM Start > Tools > Workflow > Models.
2. Click Create → Create Model and name it Set Page Title in Title Component.
3. Open the workflow for editing. Drag and Drop a process step
4. Edit the process step and add a title (can be anything), description. On the process
tab, select your worklow (the name of the workflow will the
process.label present in your workflow class) from the dropdown in the process field
5. Check the handler advance, this will allow the workflow to move forward automatically
after the process step completes

After the workflow model has been created, you need to create a custom workflow process
step.

To trigger this workflow when a page is created:

Go to AEM Start > Tools > Workflow > Launchers.
Click Create Launcher:
1. Event Type - Created
2. NodeType -cq:Page
3. path - 'your content path, where you want the launcher to trigger the workflow'
4. Workflow - select the workflow model you created
5. Toggle Activate - this will activate your launcher
6. Save and close.
 */
/*
@Component = This marks the class as an OSGI component. The component is of type workflowProcess
process.label = defines the label for the workflow step and this label appears in the workflow
model editor when adding a process step
 */
@Component(
        service = WorkflowProcess.class,
        property = { "process.label=Set Page Title in Title Component" }
)
public class SetPageTitleWorkflow implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(SetPageTitleWorkflow.class);

    /**
     * @param workItem = represents the current item that is passed in the workflow.
     * @param workflowSession =It provides the complete capability to manage workflow models
     *                        and instances. You can also use it get resource resolver in your workflow
     * @param metaDataMap = It provide access to data map and arguments passed as an input.
     * @throws WorkflowException
     */
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        //get page path on which the workflow was triggered using workItem
        String pagePath = workItem.getWorkflowData().getPayload().toString();

        // get resource Resolver using workflowSession
        try(ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class);){
            Resource resource = resolver.getResource(pagePath);
            //check if the resource is not null
            if(resource != null) {
                Resource childResource = resource.getChild("jcr:content/root/container/title");
                //check if the childresource is not null
                if(childResource != null){
                    PageManager pageManager = resolver.adaptTo(PageManager.class);
                    String pageTitle = pageManager.getPage(pagePath).getTitle();
                    //Use ModifiableValueMap that allows modification of properties in JCR node. It extends ValueMap
                    //which provides read-only access
                    ModifiableValueMap mvm = resource.adaptTo(ModifiableValueMap.class);
                    if (mvm != null) {
                        mvm.put("jcr:title",pageTitle);
                    }
                    resolver.commit();
                }
            }
        } catch (PersistenceException e) {
            LOG.error("Error while adding cq:LastModified Property to asset", e);
        }
    }
}
