<%@ page import="com.eads.threedviewer.enums.Operation; org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="booleanOperation" controller="CADObject" name="booleanForm" class="shapeForm">
    <g:hiddenField name="object1" value=""/>
    <g:hiddenField name="object2" value=""/>
    <g:hiddenField name="type" value="COMPOUND"/>
    <fieldset class="form">
        <div class="data-fields">
            <td:textField label="Name" name="name"/>
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
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="create" value="Create" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>
