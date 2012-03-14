<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:render template="/project/confirmDelete" model="[project: project]"/>

<div class="block small left">
    <div class="block_head">
        <g:form action="index" controller="project" name="changeProject" method="get">
            <g:select name="name" from="${projects}" optionKey="name" optionValue="name" noSelection="['': 'Choose Project']" value="${project?.name}" id='selectProject'/>
        </g:form>
    </div>

    <div class="block_content">
        <div id="project">
            <ul>
                <g:set var="count" value="1" scope="page"/>
                <li id="${"phtml_" + (count)}" rel="root"><a href="#">${project.name}</a>
                    <ul>
                        <g:each in="${project.getParentCadObjects()}" var="cadObject">
                            <g:set var="count" value="${count.toInteger() + 1}" scope="page"/>
                            <g:render template="cadObject" model="[cadObject:cadObject]"/>
                        </g:each>
                    </ul>
                </li>
            </ul>
        </div>

        <div id="explode" style="display: none" title="Explode">
            <g:render template="/cadObject/explode"/>
        </div>

        <div id="operation" style="display: none" title="Boolean Operation">
            <g:render template="/cadObject/operation"/>
        </div>

        <div id="mesh" style="display: none" title="Create Mesh">
            <g:render template="/cadObject/mesh" model="[project: project]"/>
        </div>
    </div>

</div>