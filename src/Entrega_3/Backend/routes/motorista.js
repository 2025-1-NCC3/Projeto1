const express = require("express");
const router = express.Router();
const sqlite3 = require("sqlite3").verbose();
const path = require("path");

// Configura o caminho do banco
const dbPath = path.join(__dirname, "../BancoDeDados.db");
const db = new sqlite3.Database(dbPath);

// Lista todos os motoristas
router.get("/", (req, res) => {
  db.all(`SELECT * FROM Motorista`, [], (err, rows) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ motoristas: rows });
  });
});

// Busca motorista por ID
router.get("/:id", (req, res) => {
  db.get(
    `SELECT * FROM Motorista WHERE id = ?`,
    [req.params.id],
    (err, row) => {
      if (err) return res.status(500).json({ error: err.message });
      if (!row)
        return res.status(404).json({ message: "Motorista não encontrado!" });
      res.json({ motoristas: row });
    }
  );
});

// Cadastra um novo motorista
router.post("/", (req, res) => {
  const {
    nome,
    sobrenome,
    telefone,
    email,
    senha,
    modelo_do_carro,
    placa_do_carro,
    frase_de_seguranca_1,
    frase_de_seguranca_2,
    frase_de_seguranca_3,
    disponibilidade,
    status_protocolo,
  } = req.body;

  if (
    !nome ||
    !sobrenome ||
    !telefone ||
    !email ||
    !senha ||
    !modelo_do_carro ||
    !placa_do_carro ||
    !frase_de_seguranca_1 ||
    !frase_de_seguranca_2 ||
    !frase_de_seguranca_3 ||
    !disponibilidade ||
    !status_protocolo
  ) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios!" });
  }

  const sql = `
    INSERT INTO Motorista (
      nome, sobrenome, telefone, email, senha,
      modelo_do_carro, placa_do_carro,
      frase_de_seguranca_1, frase_de_seguranca_2, frase_de_seguranca_3,
      disponibilidade, status_protocolo
    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
  `;
  const params = [
    nome,
    sobrenome,
    telefone,
    email,
    senha,
    modelo_do_carro,
    placa_do_carro,
    frase_de_seguranca_1,
    frase_de_seguranca_2,
    frase_de_seguranca_3,
    disponibilidade,
    status_protocolo,
  ];

  db.run(sql, params, function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.status(201).json({
      message: "Motorista cadastrado com sucesso!",
      motorista: { id: this.lastID },
    });
  });
});

// Atualiza um motorista
router.put("/:id", (req, res) => {
  const {
    nome,
    sobrenome,
    telefone,
    email,
    senha,
    modelo_do_carro,
    placa_do_carro,
    frase_de_seguranca_1,
    frase_de_seguranca_2,
    frase_de_seguranca_3,
    disponibilidade,
    status_protocolo,
  } = req.body;

  const sql = `
    UPDATE Motorista SET 
      nome=?, sobrenome=?, telefone=?, email=?, senha=?,
      modelo_do_carro=?, placa_do_carro=?,
      frase_de_seguranca_1=?, frase_de_seguranca_2=?, frase_de_seguranca_3=?,
      disponibilidade=?, status_protocolo=?
    WHERE id=?
  `;
  const params = [
    nome,
    sobrenome,
    telefone,
    email,
    senha,
    modelo_do_carro,
    placa_do_carro,
    frase_de_seguranca_1,
    frase_de_seguranca_2,
    frase_de_seguranca_3,
    disponibilidade,
    status_protocolo,
    req.params.id,
  ];

  db.run(sql, params, function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Motorista atualizado com sucesso!" });
  });
});

// Deleta um motorista
router.delete("/:id", (req, res) => {
  db.run(`DELETE FROM Motorista WHERE id = ?`, [req.params.id], function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Motorista deletado com sucesso!" });
  });
});

module.exports = router;
