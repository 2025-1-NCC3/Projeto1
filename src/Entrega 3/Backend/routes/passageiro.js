const express = require("express");
const router = express.Router();
const sqlite3 = require("sqlite3").verbose();
const path = require("path");
const dbPath = path.join(__dirname, "..", "BancoDeDados.db");
const db = new sqlite3.Database(dbPath);

// Lista todos os Passageiros
router.get("/", (req, res) => {
  db.all(`SELECT * FROM Passageiro`, [], (err, rows) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ passageiro: rows });
  });
});

// Buscar Passageiro pelo ID
router.get("/:id", (req, res) => {
  db.get(`SELECT * FROM Passageiro WHERE id=?`, [req.params.id], (err, row) => {
    if (err) return res.status(500).json({ error: err.message });
    if (!row)
      return res.status(404).json({ message: "Passageiro não encontrado!" });
    res.json({ passageiro: row });
  });
});

// Cadastrar Passageiro
router.post("/", (req, res) => {
  const { nome, sobrenome, telefone, email, senha } = req.body;

  if (!nome || !sobrenome || !telefone || !email || !senha) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios!" });
  }

  const sql = `INSERT INTO Passageiro (nome, sobrenome, telefone, email, senha) VALUES (?, ?, ?, ?, ?)`;
  const params = [nome, sobrenome, telefone, email, senha];

  db.run(sql, params, function (err) {
    if (err) {
      if (err.message.includes("UNIQUE constraint failed")) {
        return res
          .status(409)
          .json({ error: "Telefone ou email já cadastrado!" });
      }
      return res.status(500).json({ error: err.message });
    }
    res.status(201).json({
      message: "Passageiro cadastrado com sucesso!",
      passageiro: { id: this.lastID, nome, sobrenome, telefone, email },
    });
  });
});

// Atualizar Passageiro
router.put("/:id", (req, res) => {
  const { nome, sobrenome, telefone, email, senha } = req.body;

  const sql = `
    UPDATE Passageiro SET nome=?, sobrenome=?, telefone=?, email=?, senha=?
    WHERE id=?
  `;
  const params = [nome, sobrenome, telefone, email, senha, req.params.id];

  db.run(sql, params, function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Passageiro atualizado com sucesso!" });
  });
});

// Deletar Passageiro
router.delete("/:id", (req, res) => {
  db.run(`DELETE FROM Passageiro WHERE id=?`, [req.params.id], function (err) {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Passageiro deletado com sucesso!" });
  });
});

module.exports = router;
