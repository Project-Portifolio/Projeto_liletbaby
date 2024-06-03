package br.com.liletbaby.back_end.controller;

import br.com.liletbaby.back_end.models.Produto;
import br.com.liletbaby.back_end.repository.ProdutoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("liletbaby/api")
public class ProdutosController {

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @PostMapping("/cadastro")
    @ResponseBody
    public ResponseEntity<?> cadastroProdutos(@RequestBody Map<String, String> payload){
        String name = payload.get("name"), sku = payload.get("sku"), color = payload.get("color"), size = payload.get("size");
        Integer stock = Integer.valueOf(payload.get("stock"));
        Double discount = Double.valueOf(payload.get("discount")), price = Double.valueOf(payload.get("price"));
        Produto novoProduto = new Produto(name, price, stock, sku, discount, color, size);
        this.produtoRepositorio.save(novoProduto);
        return ResponseEntity.ok("Produto criado com sucesso.");
    }
}
