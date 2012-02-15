<div class="block small left">
    <div class="block_head">
        <div class="bheadl"></div>
        <div class="bheadr"></div>
        <g:if test="${project?.name}">
        <g:form action="index" controller="project" name="changeProject">
                <g:select name="name" from="${projects}" optionKey="name" optionValue="name" noSelection="['': 'Choose Project']" value="${project?.name}"
                      id='selectProject'/>
            <g:hiddenField name="id" value="${project.id}"/>
            <g:actionSubmit style="width:52px" value="Delete" action="delete" controller="project"/>
        </g:form>
        </g:if>
    </div>

    <div class="block_content">
        <g:if test="${project}">
            <div id="project">
                <ul>
                    <g:set var="count" value="1"/>
                    <li id="${"phtml_" + (count)}"><a href="#">${project.name}</a>
                        <ul>
                            <g:each in="${project.getParentCadObjects()}" var="cadObject">
                                <g:set var="count" value="${count.toInteger() + 1}"/>
                                <li id="${"phtml_" + (count)}">
                                    <a href="${createLink(controller: 'CADObject', action: 'show', id: cadObject.id)}"
                                       class="showObject" id="${cadObject.id}">${cadObject.name}</a>
                                    <ul>
                                        <g:each in="${cadObject.subCadObjects}" var="subCadObject">
                                            <g:set var="count" value="${count.toInteger() + 1}"/>
                                            <li id="${"phtml_" + (count)}">
                                                <a href="${createLink(controller: 'CADObject', action: 'show', id: subCadObject.id)}"
                                                   class="showObject" id="${subCadObject.id}">${subCadObject.name}</a>
                                            </li>
                                        </g:each>
                                    </ul>
                                </li>
                            </g:each>

                        </ul>
                    </li>
                </ul>
            </div>
        </g:if>
        <g:else>
            Project Not Found
        </g:else>
    </div>


    <div class="bendl"></div>

    <div class="bendr"></div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        enableJsTree();
    })
</script>
