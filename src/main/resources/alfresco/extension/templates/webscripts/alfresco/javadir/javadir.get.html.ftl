<html>
<head>
    <title>Folder ${folder.displayPath}/${folder.name}</title>
</head>
<body>
<p>Alfresco ${server.edition} Edition v${server.version} : dir</p>
<p>Contents of folder ${folder.displayPath}/${folder.name}</p>
<table>
    <#list Folder as child>
        <tr>
            <td><#if child.folder.isContainer>d</#if></td>
            <td>${child.folder.name}</td>
            <td>${child.size} MB</td>
            <#if verbose>
                <td>${child.folder.properties.modifier}</td>
                <td><#if child.folder.isDocument>
            ${child.folder.properties.content.size}</#if></td>
                <td>${child.folder.properties.modified?date}</td>
                <td>${child.folder.properties.modified?date}</td>
            </#if>
        </tr>
    </#list>
</table>
</body>
</html>