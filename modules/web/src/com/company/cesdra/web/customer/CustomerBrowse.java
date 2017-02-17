package com.company.cesdra.web.customer;

import com.company.cesdra.entity.Customer;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class CustomerBrowse extends AbstractLookup {

    /**
     * The {@link CollectionDatasource} instance that loads a list of {@link Customer} records
     * to be displayed in {@link CustomerBrowse#customersTable} on the left
     */
    @Inject
    private CollectionDatasource<Customer, UUID> customersDs;

    /**
     * The {@link Datasource} instance that contains an instance of the selected entity
     * in {@link CustomerBrowse#customersDs}
     * <p/> Containing instance is loaded in {@link CollectionDatasource#addItemChangeListener}
     * with the view, specified in the XML screen descriptor.
     * The listener is set in the {@link CustomerBrowse#init(Map)} method
     */
    @Inject
    private Datasource<Customer> customerDs;

    /**
     * The {@link Table} instance, containing a list of {@link Customer} records,
     * loaded via {@link CustomerBrowse#customersDs}
     */
    @Inject
    private Table<Customer> customersTable;

    /**
     * The {@link BoxLayout} instance that contains components on the left side
     * of {@link SplitPanel}
     */
    @Inject
    private BoxLayout lookupBox;

    /**
     * The {@link BoxLayout} instance that contains buttons to invoke Save or Cancel actions in edit mode
     */
    @Inject
    private BoxLayout actionsPane;

    /**
     * The {@link FieldGroup} instance that is linked to {@link CustomerBrowse#customerDs}
     * and shows fields of the selected {@link Customer} record
     */
    @Inject
    private FieldGroup fieldGroup;

    /**
     * The {@link RemoveAction} instance, related to {@link CustomerBrowse#customersTable}
     */
    @Named("customersTable.remove")
    private RemoveAction customersTableRemove;

    @Inject
    private DataSupplier dataSupplier;

    /**
     * {@link Boolean} value, indicating if a new instance of {@link Customer} is being created
     */
    private boolean creating;

    @Override
    public void init(Map<String, Object> params) {

        /**
         * Adding {@link com.haulmont.cuba.gui.data.Datasource.ItemChangeListener} to {@link customersDs}
         * The listener reloads the selected record with the specified view and sets it to {@link customerDs}
         */
        customersDs.addItemChangeListener(e -> {
            if (e.getItem() != null) {
                Customer reloadedItem = dataSupplier.reload(e.getDs().getItem(), customerDs.getView());
                customerDs.setItem(reloadedItem);
            }
        });

        /**
         * Adding {@link CreateAction} to {@link customersTable}
         * The listener removes selection in {@link customersTable}, sets a newly created item to {@link customerDs}
         * and enables controls for record editing
         */
        customersTable.addAction(new CreateAction(customersTable) {
            @Override
            protected void internalOpenEditor(CollectionDatasource datasource, Entity newItem, Datasource parentDs, Map<String, Object> params) {
                customersTable.setSelected(Collections.emptyList());
                customerDs.setItem((Customer) newItem);
                enableEditControls(true);
            }
        });

        /**
         * Adding {@link EditAction} to {@link customersTable}
         * The listener enables controls for record editing
         */
        customersTable.addAction(new EditAction(customersTable) {
            @Override
            protected void internalOpenEditor(CollectionDatasource datasource, Entity existingItem, Datasource parentDs, Map<String, Object> params) {
                if (customersTable.getSelected().size() == 1) {
                    enableEditControls(false);
                }
            }
        });

        /**
         * Setting {@link RemoveAction#afterRemoveHandler} for {@link customersTableRemove}
         * to reset record, contained in {@link customerDs}
         */
        customersTableRemove.setAfterRemoveHandler(removedItems -> customerDs.setItem(null));

        disableEditControls();
    }

    /**
     * Method that is invoked by clicking Save button after editing an existing or creating a new record
     */
    public void save() {
        getDsContext().commit();

        Customer editedItem = customerDs.getItem();
        if (creating) {
            customersDs.includeItem(editedItem);
        } else {
            customersDs.updateItem(editedItem);
        }
        customersTable.setSelected(editedItem);

        disableEditControls();
    }

    /**
     * Method that is invoked by clicking Save button after editing an existing or creating a new record
     */
    public void cancel() {
        Customer selectedItem = customersDs.getItem();
        if (selectedItem != null) {
            Customer reloadedItem = dataSupplier.reload(selectedItem, customerDs.getView());
            customersDs.setItem(reloadedItem);
        } else {
            customerDs.setItem(null);
        }

        disableEditControls();
    }

    /**
     * Enabling controls for record editing
     * @param creating indicates if a new instance of {@link Customer} is being created
     */
    private void enableEditControls(boolean creating) {
        this.creating = creating;
        initEditComponents(true);
        fieldGroup.requestFocus();
    }

    /**
     * Disabling editing controls
     */
    private void disableEditControls() {
        initEditComponents(false);
        customersTable.requestFocus();
    }

    /**
     * Initiating edit controls, depending on if they should be enabled/disabled
     * @param enabled if true - enables editing controls and disables controls on the left side of the splitter
     *                if false - visa versa
     */
    private void initEditComponents(boolean enabled) {
        fieldGroup.setEditable(enabled);
        actionsPane.setVisible(enabled);
        lookupBox.setEnabled(!enabled);
    }
}