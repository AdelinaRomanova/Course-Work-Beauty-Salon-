package org.example.controller;

import org.example.model.Purchase;
import org.example.model.PurchaseDetail;
import org.example.model.dto.PurchaseDetailDto;
import org.example.model.dto.PurchaseDto;
import org.example.service.PurchaseService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<PurchaseDto> addPurchase(@RequestBody final PurchaseDto purchaseDto) {
        Purchase purchase = purchaseService.addPurchase(Purchase.from(purchaseDto));
        return new ResponseEntity<>(PurchaseDto.from(purchase), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseDto>> getPurchases() {
        List<Purchase> purchases = purchaseService.getPurchases();
        List<PurchaseDto> purchaseDto = purchases.stream().map(PurchaseDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(purchaseDto, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<PurchaseDto> getPurchase(@PathVariable final Long id) {
        Purchase purchase = purchaseService.getPurchase(id);
        return new ResponseEntity<>(PurchaseDto.from(purchase), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<PurchaseDto> deletePurchase(@PathVariable final Long id) {
        Purchase purchase = purchaseService.deletePurchase(id);
        return new ResponseEntity<>(PurchaseDto.from(purchase), HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<PurchaseDto> editPurchase(@PathVariable final Long id,
                                                    @RequestBody final PurchaseDto purchaseDto) {
        Purchase purchase = purchaseService.editPurchase(id, Purchase.from(purchaseDto));
        return new ResponseEntity<>(PurchaseDto.from(purchase), HttpStatus.OK);
    }

    @PostMapping(value = "{purchaseId}/procedures/add/{procedureId}/{count}")
    public ResponseEntity<PurchaseDto> addProcedureToPurchase(@PathVariable final Long purchaseId,
                                                              @PathVariable final Long procedureId,
                                                              @PathVariable final int count) {
        Purchase purchase = purchaseService.addProcedureToPurchase(purchaseId, procedureId, count);
        return new ResponseEntity<>(PurchaseDto.from(purchase), HttpStatus.OK);
    }

    @GetMapping(value = "{id}/procedures")
    public ResponseEntity<PurchaseDetailDto> getPurchaseDetails(@PathVariable final Long id) {
        List<PurchaseDetail> details = purchaseService.getPurchase(id).getProcedures();
        List<PurchaseDetailDto> detailsDto = details.stream().map(PurchaseDetailDto::from).collect(Collectors.toList());
        return new ResponseEntity(detailsDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "{purchaseId}/procedures/remove/{procedureId}")
    public ResponseEntity<PurchaseDto> removeProcedureFromPurchase(@PathVariable final Long purchaseId,
                                                                   @PathVariable final Long procedureId) {
        Purchase purchase = purchaseService.removeProcedureFromPurchase(purchaseId, procedureId);
        return new ResponseEntity<>(PurchaseDto.from(purchase), HttpStatus.OK);
    }
}