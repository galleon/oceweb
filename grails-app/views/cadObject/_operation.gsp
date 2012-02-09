<%@ page import="com.eads.threedviewer.enums.Operation; org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="booleanOperation" controller="CADObject" name="booleanForm" class="shapeForm">
    <g:hiddenField name="object1" value=""/>
    <g:hiddenField name="object2" value=""/>
    <fieldset class="form" style="width: 420px;">
        <h3>Boolean Operation</h3>   <br/>

        <div class="fieldcontain required">
            <label for="name">
                Name
                <span class="required-indicator">*</span>
            </label>
            <g:textField name="name" value="Compound" required="true"/>
        </div>

        <div class="fieldcontain required">
            <label>
                Object 1
            </label>
            <span id="obj_1"></span>
        </div>

        <div class="fieldcontain required">
            <label>
                Object 2
            </label>
            <span id="obj_2"></span>
        </div>
        <div class="fieldcontain required">
            <label>
               Operation
            </label>
            <g:select name="operation" from="${Operation.values()}"/>
        </div>

    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="create" value="Create" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>
