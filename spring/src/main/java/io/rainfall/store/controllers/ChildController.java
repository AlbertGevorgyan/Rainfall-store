package io.rainfall.store.controllers;

import io.rainfall.store.dataset.ChildDataset;
import io.rainfall.store.dataset.ChildRecord;
import io.rainfall.store.dataset.Record;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;


abstract class ChildController<
            V,
            R extends ChildRecord<V, PR>,
            PR extends Record<?>,
            S extends ChildDataset<V, R, ?, PR, ?>
        >
        extends DatasetController<R, S> {

    ChildController(S dataset, String valueName, String path) {
        super(dataset, valueName, path);
    }

    ResponseEntity<?> post(long parentId, V value) {
        long id = dataset().save(parentId, value)
                .getId();
        return post(id, CREATED);
    }

    ModelAndView getByParentId(ModelMap model, long parentId) {
        List<R> records = dataset().findByParentId(parentId);
        return getRecords(model, records);
    }

    ModelAndView getRecords(ModelMap model, List<R> records) {
        String viewName = path.replaceAll("^/", "");
        model.addAttribute(viewName, records);
        return new ModelAndView(viewName, model);
    }
}
