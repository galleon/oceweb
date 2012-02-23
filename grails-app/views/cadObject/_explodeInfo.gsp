<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="saveExplode" class="shapeForm" controller="CADObject" name="explodeForm" >
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo" model="[cadObject:cadObject]"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:if test="${cadObject?.id}">
            <g:submitButton name="submit" value="Update" class="save"/>
        </g:if>
        <g:else>
            <g:submitButton value="Save" name="submit" class="save"/>
        </g:else>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>