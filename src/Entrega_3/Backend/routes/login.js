const express = require("express");
const router = express.Router();
const sqlite3 = require("sqlite3").verbose();
const path = require("path");

// Caminho do banco
const dbPath = path.join(__dirname, "../BancoDeDados.db");
const db = new sqlite3.Database(dbPath);

router.post("/motorista", (req, res) => {
  const { email, senha } = req.body;

  if (!email || !senha) {
    return res.status(400).json({ error: "Email e senha são obrigatórios!" });
  }

  db.get(`SELECT * FROM Motorista WHERE email = ?`, [email], (err, row) => {
    if (err) return res.status(500).json({ error: err.message });
    if (!row)
      return res.status(404).json({ message: "Motorista não encontrado!" });

    if (row.senha !== senha) {
      return res.status(401).json({ message: "Senha incorreta!" });
    }

    res.json({
      message: "Login realizado com sucesso!",
      tipo: "motorista",
      usuario: {
        id: row.id,
        nome: row.nome,
        email: row.email,
      },
    });
  });
});

router.post("/passageiro", (req, res) => {
  const { email, senha } = req.body;

  if (!email || !senha) {
    return res.status(400).json({ error: "Email e senha são obrigatórios!" });
  }

  db.get(`SELECT * FROM Passageiro WHERE email = ?`, [email], (err, row) => {
    if (err) return res.status(500).json({ error: err.message });
    if (!row)
      return res.status(404).json({ message: "Passageiro não encontrado!" });

    if (row.senha !== senha) {
      return res.status(401).json({ message: "Senha incorreta!" });
    }

    res.json({
      message: "Login realizado com sucesso!",
      tipo: "passageiro",
      usuario: {
        id: row.id,
        nome: row.nome,
        email: row.email,
      },
    });
  });
});

module.exports = router;
