<div class="block small left">
    <div class="block_head">
        <div class="bheadl"></div>

        <div class="bheadr"></div>
        <g:form action="index" controller="project" name="changeProject">
            <g:select name="name" from="${projects}" optionKey="name" optionValue="name" style="width: 80%;" noSelection="['': 'Choose Project']" value="${project?.name}"
                      id='selectProject'/>
        </g:form>
    </div>

    <div class="block_content">
        <g:if test="${project}">
            <div id="project">
                <ul>
                    <g:set var="count" value="1"/>
                    <li id="${"phtml_" + (count)}"><a href="#">${project.name}</a>
                        <ul>
                            <g:each in="${project.cadObjects}" var="cadObject">
                                <g:set var="count" value="${count.toInteger() + 1}"/>
                                <li id="${"phtml_" + (count)}">
                                    <a href="${createLink(controller: 'CADObject', action: 'show', id: cadObject.id)}" class="showObject" id="${cadObject.id}">${cadObject.name}</a>
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
