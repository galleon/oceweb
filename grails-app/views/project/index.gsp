<!doctype html>
<html lang="en">
<head>
    <title>Project</title>
    <meta name="layout" content="main"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">

</head>

<body>
<div>
    <fieldset class="form">
        <div id="data" style="width: 29%;float: left;">
            <div id="status" role="complementary">
                <ul>
                    <g:each in="${projects}" var="project">
                        <li>
                            <span style="float: left;">
                                <a href="#" class="showFace">${project.name}</a>
                            </span>
                        </li>
                    </g:each>
                </ul>
            </div>

        </div>

        <div id="content" style="width: 70%;float: right;">

        </div>

    </fieldset>
    <fieldset class="buttons">
        <a href="#" id="projects" style="float: right;">
            <img src="${resource(dir: 'images', file: 'folder.png')}" alt="projects">
        </a>
    </fieldset>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $(".showFace").click(function () {
            var url = $(this).attr('rel');
            $(".active-face").removeClass('active-face');
            $(this).addClass('active-face');
            return false;
        })
    })
</script>
</body>
</html>
