<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="generateUnv" controller="CADObject" name="computeForm">
    <fieldset class="form">
        <g:hiddenField name="id" id="cadObjectId" value=""/>
        <td:textField name="frequecy" label="Frequency (MHZ)"/>
        <div style="border-bottom: 1px solid #2C2C2C">
            <div class="angle">
                <label>Angles</label>
            </div>

            <div>
                <label>Theta</label> <span class="required-indicator">*</span>
                <input type="text" required="" value="" id="theta" name="theta" class="small">
                <label>Phi</label> <span class="required-indicator">*</span>
                <input type="text" required="" value="" id="phi" name="theta" class="small">

                <div class="polarisation">
                    <label>Polarisation</label> <span class="required-indicator">*</span>
                    <g:select name="polarisation" from="${['Vertical','Horizontal','Vertical & Horizontal']}"/>
                </div>
            </div>
        </div>

        <div class="fieldcontain required">
            <table border="0" cellpadding="4" cellspacing="0">
                <tr>
                    <td class="first">Domains</td>
                    <td>Epsilon</td>
                    <td>Sigma</td>
                    <td>Mu</td>
                    <td>Sigma'</td>
                </tr>
                <tr>
                    <td>Vacuum</td>
                    <td><g:textField name="epsilon" class="small"/></td>
                    <td><g:textField name="sigma" class="small"/></td>
                    <td><g:textField name="mu" class="small"/></td>
                    <td><g:textField name="sigmaNot" class="small"/></td>
                </tr>
                <tr>
                    <td>Vacuum</td>
                    <td><g:textField name="epsilon1" class="small"/></td>
                    <td><g:textField name="sigma" class="small"/></td>
                    <td><g:textField name="mu1" class="small"/></td>
                    <td><g:textField name="sigmaNot1" class="small"/></td>
                </tr>
            </table>
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
        <g:actionSubmit value="Run" id="submit" action="generateUnv" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>
