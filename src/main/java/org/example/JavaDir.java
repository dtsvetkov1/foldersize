package org.example;

import java.io.Serializable;
import java.util.*;


import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
//import org.alfresco.service.NodeRef;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.*;
import org.alfresco.service.namespace.QName;
import org.springframework.extensions.webscripts.*;


public class JavaDir extends DeclarativeWebScript
{


    private FileFolderService fileFolderService;

    public void setFileFolderService(FileFolderService fileFolderService)
    {
        this.fileFolderService = fileFolderService;
    }


    private NodeService nodeService;

    public void setNodeService(NodeService nodeService)
    {
        this.nodeService = nodeService;
    }

    private Repository repository;

    public void setRepository(Repository repository)
    {
        this.repository = repository;
    }


    float foldersize(NodeRef directory, Float length) {

        length = Float.valueOf(0);
        for (ChildAssociationRef child: nodeService.getChildAssocs(directory)) {

            ContentData contentData = (ContentData) nodeService.getProperty(child.getChildRef(), ContentModel.PROP_CONTENT);
            try {
                length += Float.valueOf(contentData.getSize())/(1024*1024);
            } catch (Exception e) {
                length +=  foldersize(child.getChildRef(), length);
            }

        }
        return length;
    }

    protected Map<String, Object> executeImpl(WebScriptRequest req,
                                              Status status, Cache cache)
    {

        NodeRef folder;
        Float size = Float.valueOf(0);
        Float local_size;


        // extract folder listing arguments from URI
        String verboseArg = req.getParameter("verbose");
        Boolean verbose = Boolean.parseBoolean(verboseArg);

        Map<String, String> templateArgs =
                req.getServiceMatch().getTemplateVars();
        String folderPath = templateArgs.get("folderpath");

        if (folderPath.equals("Company Home")){

            folder = repository.getCompanyHome();

        }
        else {
            String nodePath = "workspace/SpacesStore/" + folderPath;
            folder = repository.findNodeRef("path", nodePath.split("/"));
        }


        List<Map> total_list = new ArrayList<>();

        List<ChildAssociationRef> ChildAss = nodeService.getChildAssocs(folder);

        for (ChildAssociationRef child: ChildAss) {

            size = Float.valueOf(0);

            Map<String, Object> Folder = new HashMap<>();

            NodeRef cnode = child.getChildRef();

            Map<QName, Serializable> props = nodeService.getProperties(cnode);

            Folder.put("folder", cnode);
            Folder.put("size", foldersize(cnode, size));

            total_list.add(Folder);
        }


        // construct model for response template to render
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("verbose", verbose);
        model.put("folder", folder);
        model.put("Folder", total_list);
        return model;
    }

}