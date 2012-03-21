<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="explode" controller="CADObject" name="computeForm" class="shapeForm">
    <fieldset class="form">
        <g:hiddenField name="id" id="cadObjectId" value=""/>
        <g:hiddenField name="type" value="${ShapeType.COMPUTE}"/>
        <td:textField name="frequecy" label="Frequency (MHZ)"/>
        <div style="border-bottom: 1px solid #2C2C2C">
            <div style="float: left; width: 50%;">
                <label>Angles</label>
            </div>
            Theta <span class="required-indicator">*</span>
            <input type="text" required="" value="" id="theta" name="theta" class="small">
            Phi <span class="required-indicator">*</span>
            <input type="text" required="" value="" id="phi" name="theta" class="small">

            <div class="polarisation">
                Polarisation <span class="required-indicator">*</span>
                <g:select name="polarisation" from="${['Vertical','Horizontal','Vertical & Horizontal']}"/>
            </div>
        </div>

        <div class="fieldcontain required">
            <label for="postProcessing">
                Post Processing
                <span class="required-indicator">*</span>
            </label>
            <g:select name="postProcessing" from="${['Coucha','Porche','Anten']}" class="postProcessing"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <input type="button" name="create" value="Run" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>
