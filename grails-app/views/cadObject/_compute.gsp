<%@ page import="com.eads.threedviewer.enums.ProcessingType; com.eads.threedviewer.enums.PolarisationType" %>
<g:form action="runSimulation" controller="CADObject" name="computeForm" class="shapeForm">
    <fieldset class="form">
        <g:hiddenField name="id" id="cadObjectId" value=""/>
        <td:textField name="frequency" label="Frequency (MHZ)"/>
        <div style="border-bottom: 1px solid #2C2C2C">
            <div class="angle" style="width:49%;">
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
            <table border="0" cellpadding="2" cellspacing="0">
                <tr>
                    <td width="100">Domains</td>
                    <td width="85">Epsilon</td>
                    <td width="85">Sigma</td>
                    <td width="85">Mu</td>
                    <td width="85">Sigma</td>
                    <td width="25">&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr id="domainContent" class="domainContent">
                    <td id='domainName'>Vacuum</td>
                    <td><g:textField name="epsilon.0" class="small"/></td>
                    <td><g:textField name="sigma.0" class="small"/></td>
                    <td><g:textField name="mu.0" class="small"/></td>
                    <td><g:textField name="sigma1.0" class="small"/></td>
                    <td valign="middle" align="center"><a href="#"><img src="${resource(dir: 'images', file: 'add.png')}" alt="Add" class="addContent"></a></td>
                    <td valign="middle" align="center"><a href="#"
                                                     style="display: none"><img src="${resource(dir: 'images', file: 'remove.png')}" alt="Remove" class="removeContent"></a></td>
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
