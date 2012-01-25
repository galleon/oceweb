<div id="status" role="complementary">
    <ul>
        <g:each in="${shapes}" var="shape">
            <li>
                ${shape.name}
                <span>
                    <a href="#" rel="${createLink(controller: 'util', action: 'showShape', id: shape.id)}" class="showFace">Show</a>
                </span>
            </li>
        </g:each>
    </ul>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $(".showFace").click(function () {
            var url = $(this).attr('rel');
            showShape(url, 'content');
        })
        return false;
    })
</script>