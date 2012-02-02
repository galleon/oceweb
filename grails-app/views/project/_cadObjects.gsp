<div class="block small left">
    <div class="block_head">
        <div class="bheadl"></div>

        <div class="bheadr"></div>

        <h2>Projects</h2>
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
                                    <a href="${createLink(controller: 'CADObject', action: 'show', id: cadObject.id)}" class="showObject">${cadObject.name}</a>
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
        $("#project").jstree({"plugins":["themes", "html_data", "ui", "crrm", "hotkeys"], "themes":{"theme":"apple",
            "icons":true,
            "url":"${resource(dir:'css', file: 'style.css')}"
        }, "core":{ "initially_open":[ "phtml_1" ] }}).bind("select_node.jstree", function (event, data) {
                    showShape(data.rslt.obj.children().filter('a').attr('href'), 'content', {}, {})
                })
    })
</script>
