<%@ page import="org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="explode" controller="CADObject" name="explodeForm">
    <g:hiddenField name="id" id="cadObjectId" value=""/>
    <fieldset class="form" style="width: 420px;">
        <h3>Explode Form</h3>   <br/>
        <div class="data-fields">
            <div class="fieldcontain required">
                <label for="CADShape">
                    Shape
                    <span class="required-indicator">*</span>
                </label>
                <g:select style="float: right;" from="${CADShapeEnum.FACE}" name="CADShape"  required="true"/>
            </div>

        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="create" value="Create"  class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>
