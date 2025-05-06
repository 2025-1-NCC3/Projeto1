const express = require("express");
const router = express.Router();
const sqlite3 = require("sqlite3").verbose();
const path = require("path");
const dbPath = path.join(__dirname, "..", "BancoDeDados.db");
const db = new sqlite3.Database(dbPath);

// --------------------- VIAGEM ---------------------

// Lista todas as Viagens
router.get("/", (req, res) => {
  db.all(`SELECT * FROM Viagem`, [], (err, rows) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ viagem: rows });
  });
});

// Lista todas as Viagens de um Passageiro
router.get("/passageiro/:id", (req, res) => {
  const sql = `SELECT * FROM Viagem WHERE passageiro_id = ?`;
  db.all(sql, [req.params.id], (err, rows) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ viagens: rows });
  });
});

// Lista todas as Viagens de um Motorista
router.get("/motorista/:id", (req, res) => {
  const sql = `SELECT * FROM Viagem WHERE motorista_id = ?`;
  db.all(sql, [req.params.id], (err, rows) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ viagens: rows });
  });
});

// Lista todas as viagens disponíveis
router.get("/disponiveis", (req, res) => {
  const sql = `SELECT * FROM Viagem WHERE status = 'disponivel'`;
  db.all(sql, [], (err, rows) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ viagens: rows });
  });
});

// Encontra uma Viagem pelo ID
router.get("/:id", (req, res) => {
  db.get(`SELECT * FROM Viagem WHERE id=?`, [req.params.id], (err, row) => {
    if (err) return res.status(500).json({ error: err.message });
    if (!row)
      return res.status(404).json({ message: "Viagem não encontrada!" });
    res.json({ viagem: row });
  });
});

// Adiciona uma Viagem
router.post("/", (req, res) => {
  const {
    endereco_de_partida,
    endereco_de_chegada,
    data_hora_de_partida,
    data_hora_de_chegada,
    status,
    motorista_id,
    passageiro_id,
  } = req.body;

  if (
    !endereco_de_partida ||
    !endereco_de_chegada ||
    !data_hora_de_partida ||
    !data_hora_de_chegada ||
    !status ||
    !motorista_id ||
    !passageiro_id
  ) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios!" });
  }

  const sql = `
    INSERT INTO Viagem (endereco_de_partida, endereco_de_chegada, data_hora_de_partida, data_hora_de_chegada, status, motorista_id, passageiro_id)
    VALUES (?, ?, ?, ?, ?, ?, ?)
  `;
  const params = [
    endereco_de_partida,
    endereco_de_chegada,
    data_hora_de_partida,
    data_hora_de_chegada,
    status,
    motorista_id,
    passageiro_id,
  ];

  db.run(sql, params, function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.status(201).json({
      message: "Viagem cadastrada com sucesso!",
      id: this.lastID,
    });
  });
});

// Atualiza uma Viagem
router.put("/:id", (req, res) => {
  const {
    endereco_de_partida,
    endereco_de_chegada,
    data_hora_de_partida,
    data_hora_de_chegada,
    status,
    motorista_id,
    passageiro_id,
  } = req.body;

  const sql = `
    UPDATE Viagem SET endereco_de_partida=?, endereco_de_chegada=?, data_hora_de_partida=?, data_hora_de_chegada=?, status=?, motorista_id=?, passageiro_id=?
    WHERE id=?
  `;
  const params = [
    endereco_de_partida,
    endereco_de_chegada,
    data_hora_de_partida,
    data_hora_de_chegada,
    status,
    motorista_id,
    passageiro_id,
    req.params.id,
  ];

  db.run(sql, params, function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Viagem atualizada com sucesso!" });
  });
});

// Deleta uma Viagem
router.delete("/:id", (req, res) => {
  db.run(`DELETE FROM Viagem WHERE id=?`, [req.params.id], function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Viagem deletada com sucesso!" });
  });
});

module.exports = router;
