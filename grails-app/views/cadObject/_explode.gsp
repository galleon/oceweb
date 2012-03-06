<%@ page import="org.jcae.opencascade.jni.TopAbs_ShapeEnum" %>
<g:form action="explode" controller="CADObject" name="explodeForm">
    <g:hiddenField name="id" id="cadObjectId" value=""/>
    <fieldset class="form">
        <g:hiddenField name="type" value="EXPLODE"/>
        <div class="data-fields">
            <div class="fieldcontain required">
                <label for="shape">
                    Shape
                    <span class="required-indicator">*</span>
                </label>
                <g:select style="float: right;" from="${[TopAbs_ShapeEnum.FACE]}" name="shape" required="true"/>
            </div>

        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="create" value="Create" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>
