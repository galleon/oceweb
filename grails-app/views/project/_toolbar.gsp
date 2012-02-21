<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:if test="${project}">
    <g:hiddenField name="projectId" value="${project.id}"/>
    <ul class="toolbox-list clearfix">
        <li><a title="Create Sphere" class="sphere model ajax"
               href="${createLink(controller: 'CADObject',action: 'createSphere',params: ['project.id':project.id,'type':ShapeType.SPHERE])}"></a></li>
        <li><a title="Create Cube" class="cube model ajax"
               href="${createLink(controller: 'CADObject',action: 'createCube',params: ['project.id':project.id,'type':ShapeType.CUBE])}"></a></li>
        <li><a title="Create Cylinder" class="cylinder model ajax"
               href="${createLink(controller: 'CADObject',action: 'createCylinder',params:
                ['project.id':project.id,'type':ShapeType.CYLINDER])}"></a></li>
        <li><a title="Create Cone" id="coneLink" class="cone model ajax"
               href="${createLink(controller: 'CADObject',action: 'createCone',params:
                ['project.id':project.id,'type':ShapeType.CONE])}"></a></li>
        <li><a title="Import File" class="plane model ajax"
               href="${createLink(controller: 'CADObject',action: 'createFile',params: ['project.id':project.id,'type':ShapeType.FILE])}"
               id="import"></a></li>
    </ul>

    <div id="templateHolder" style="display: none;" ></div>
</g:if>
