<%@ page import="com.eads.threedviewer.enums.ShapeType; org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="saveMesh" class="shapeForm" controller="CADObject" name="meshForm">
    <g:render template="/cadObject/shapeInfo" model="[cadObject:cadObject]"/>
    <fieldset class="buttons">
        <g:submitButton name="submit" value="Save" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>