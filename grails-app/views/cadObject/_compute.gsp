<%@ page import="com.eads.threedviewer.enums.ProcessingType; com.eads.threedviewer.enums.PolarisationType" %>
<g:form action="runSimulation" controller="CADObject" name="computeForm" class="shapeForm">
    <fieldset class="form">
        <g:hiddenField name="id" id="cadObjectId" value=""/>
        <td:textField name="frequency" label="Frequency (MHZ)"/>
        <div style="border-bottom: 1px solid #2C2C2C">
            <div class="angle">
                <label>Angles</label>
            </div>

            <div>
                <label>Theta</label> <span class="required-indicator">*</span>
                <input type="text" required="" value="" id="theta" name="theta" class="small">
                <label>Phi</label> <span class="required-indicator">*</span>
                <input type="text" required="" value="" id="phi" name="phi" class="small">

                <div class="polarisationType">
                    <label>Polarisation</label> <span class="required-indicator">*</span>
                    <g:select name="polarisationType" from="${PolarisationType.list()}" optionKey="key" optionValue="value"/>
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
                    <td><g:textField name="epsilon1" class="small"/></td>
                    <td><g:textField name="sigma1" class="small"/></td>
                    <td><g:textField name="mu1" class="small"/></td>
                    <td><g:textField name="sigma_1" class="small"/></td>
                </tr>
                <tr>
                    <td>Vacuum</td>
                    <td><g:textField name="epsilon2" class="small"/></td>
                    <td><g:textField name="sigma2" class="small"/></td>
                    <td><g:textField name="mu2" class="small"/></td>
                    <td><g:textField name="sigma_2" class="small"/></td>
                </tr>
            </table>
        </div>

        <div class="fieldcontain required">
            <label for="processingType">
                Post Processing
                <span class="required-indicator">*</span>
            </label>
            <g:select name="processingType" from="${ProcessingType.list()}" class="postProcessing" optionKey="key" optionValue="value"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Run" id="submit" action="runSimulation" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>
