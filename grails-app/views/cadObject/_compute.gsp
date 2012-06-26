<%@ page import="com.eads.threedviewer.enums.ProcessingType; com.eads.threedviewer.enums.PolarisationType" %>


<g:form action="runSimulation" controller="CADObject" name="computeForm" class="shapeForm">
    <fieldset class="form">
        <g:hiddenField name="id" id="cadObjectId" value=""/>
        <td:textField name="frequency" label="Frequency (MHZ)"/>
        <div class="fieldcontain required">
            <label>Angles</label>
        </div>

        <div class="fieldcontain required">
            <table border="0" cellpadding="2" cellspacing="0">
                <tr>
                    <td><label>Theta</label> <span class="required-indicator">*</span></td>
                    <td><input type="text" required="" value="" id="theta" name="theta" class="small"></td>
                    <td><label>Phi</label> <span class="required-indicator">*</span></td>
                    <td><input type="text" required="" value="" id="phi" name="phi" class="small"></td>
                    <td><label>Polarisation</label> <span class="required-indicator">*</span></td>
                    <td><g:select name="polarisationType" from="${PolarisationType.list()}" optionKey="key"
                                  optionValue="value"/></td>
                </tr>
            </table>

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
                <g:set var="totalCount" value="${"0".toInteger()}"/>
                <g:each in="${cadObject}" var="subCadObject">
                    <g:set var="totalCount" value="${totalCount?.toInteger() + 1}"/>
                    <g:if test="${totalCount == 1}">
                        <tr id="domainContent" class="domainContent" >
                    </g:if>
                    <g:else>
                        <tr  class="domainContent" style="display:none" id="compute_${subCadObject.name}">
                    </g:else>
                    <td id='domainName'>${subCadObject.name}</td>
                    <td><g:textField name="epsilon.${subCadObject.name}" class="small"/></td>
                    <td><g:textField name="sigma.${subCadObject.name}" class="small"/></td>
                    <td><g:textField name="mu.${subCadObject.name}" class="small"/></td>
                    <td><g:textField name="sigma1.${subCadObject.name}" class="small"/></td>
                    <g:if test="${totalCount != cadObject.size()}">
                        <td valign="middle" align="center"><a href="javascript:void(0)" id="${subCadObject.name}"
                                                              class="addComputeDialog"><img
                                    src="${resource(dir: 'images', file: 'add.png')}" alt="Add">
                        </a>
                        </td>
                    </g:if>
                    <g:else>
                        <td>&nbsp;</td>
                    </g:else>

                    <g:if test="${totalCount != 1}">
                        <td valign="middle" align="center"><a href="javascript:void(0)"

                                                              id="${subCadObject.name}" class="removeComputeDialog"><img
                                    src="${resource(dir: 'images', file: 'remove.png')}" alt="Remove"></a>
                        </td>
                    </g:if>
                    <g:else>
                        <td>&nbsp;</td>
                    </g:else>
                    </tr>
                </g:each>
            </table>
        </div>

        <div class="fieldcontain required">
            <label for="processingType">
                Post Processing
                <span class="required-indicator">*</span>
            </label>
            <g:select name="processingType" from="${ProcessingType.list()}" class="postProcessing" optionKey="key"
                      optionValue="value"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Run" id="submit" action="runSimulation" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>
