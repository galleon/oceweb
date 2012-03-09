<%@ page import="com.eads.threedviewer.enums.ShapeType; org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="saveMesh" class="shapeForm" controller="CADObject" name="meshForm">
    <g:render template="/cadObject/shapeInfo" model="[cadObject:cadObject]"/>
    <div class="fieldcontain required" style="padding-bottom: 25px">
        <label for="colorSelector">
            Color <span class="required-indicator">*</span>
        </label>
        <g:set var="color" value="#${cadObject.color}"/>
        <span id="colorSelector"><div style="background-color: ${color}; "></div></span>
        <g:hiddenField name="color"/>
    </div>
    <fieldset class="buttons">
        <g:submitButton name="submit" value="Save" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>
<script type="text/javascript">
    $(document).ready(function () {
        $('#colorSelector').ColorPicker({
            color:"${color}",
            onShow:function (colpkr) {
                $(colpkr).fadeIn(500);
                return false;
            },
            onHide:function (colpkr) {
                $(colpkr).fadeOut(500);
                return false;
            },
            onChange:function (hsb, hex, rgb) {
                $('#colorSelector div').css('background-color', '#' + hex);
                $("#color").val(hex)
            }
        });
    })
</script>
