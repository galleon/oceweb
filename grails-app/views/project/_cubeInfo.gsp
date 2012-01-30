<fieldset class="form">
    <div class="data-fields">
        <g:render template="shapeInfo"/>
        <div class="fieldcontain required">
            <label for="width">
                Width
                <span class="required-indicator">*</span>
            </label>
            <g:textField name="width" value=""/>
        </div>
    </div>
</fieldset>
<fieldset class="buttons">
    <input type="button" name="create" class="save" value="Create"/>
</fieldset>