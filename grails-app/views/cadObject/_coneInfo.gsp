<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="saveCone" class="shapeForm" controller="CADObject" name="coneForm" >
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo" model="[cadObject:cadObject]"/>
            <g:render template="/cadObject/axisInfo" model="[x:cadObject?.x,y:cadObject?.y,z:cadObject?.z]"/>
            <td:textField label="Base Radius" name="baseRadius" value="${cadObject?.baseRadius}"/>
            <td:textField label="Height" name="height" value="${cadObject?.height}"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:if test="${cadObject?.id}">
            <g:actionSubmit value="Update" id="submit" action="saveCone" class="save"/>
        </g:if>
        <g:else>
            <g:actionSubmit value="Save" id="submit" action="saveCone" class="save"/>
        </g:else>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>