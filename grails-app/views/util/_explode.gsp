<%@ page import="org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="explode" controller="CADObject" name="explodeForm" class="shapeForm">
    <g:hiddenField name="id" id="cadObjectId" value=""/>
    <fieldset class="form">
        <h3>Explode Form</h3>   <br/>
        <div class="data-fields">

        <label for="CADShape">
            CADShape
        </label>
            <g:select style="float: right;" from="${CADShapeEnum.FACE}" name="CADShape"  required="true"/>

        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="create" value="Create"  class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>
