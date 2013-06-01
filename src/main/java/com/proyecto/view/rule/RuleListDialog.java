package com.proyecto.view.rule;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.springframework.beans.factory.annotation.Autowired;

import com.common.util.annotations.View;
import com.common.util.exception.CheckedException;
import com.common.util.holder.HolderApplicationContext;
import com.proyecto.model.rule.Rule;
import com.proyecto.service.rule.RuleService;

/**
 * La ventana donde vamos a desplegar el listado de las reglas que vamos a tener dentro del sistema.
 * 
 * @author Guillermo Mazzali
 * @version 1.0
 */
@View
public class RuleListDialog extends JDialog {

	private static final long serialVersionUID = 4956186132310235753L;

	/**
	 * El servicio de reglas.
	 */
	@Autowired
	private RuleService ruleService;

	/**
	 * La ventana de edici�n de reglas.
	 */
	@Autowired
	private RuleFormDialog ruleFormDialog;

	/**
	 * Los modelos de las listas de reglas y sus listas.
	 */
	private DefaultListModel<Rule> ruleModelList;
	private JList<Rule> ruleList;

	/**
	 * Constructor de la ventana de listado de reglas.
	 */
	public RuleListDialog() {
		super();
		this.init();
	}

	/**
	 * La funci�n encargada de inicializar la ventana.
	 */
	public void init() {
		this.setBounds(100, 100, 601, 395);
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(contentPanel, BorderLayout.CENTER);

		JScrollPane rulesScrollPane = new JScrollPane();
		rulesScrollPane.setBounds(10, 11, 457, 340);
		contentPanel.add(rulesScrollPane);

		this.ruleModelList = new DefaultListModel<Rule>();
		this.ruleList = new JList<Rule>();
		this.ruleList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				RuleListDialog.this.modifyRule();
			}
		});
		this.ruleList.setModel(this.ruleModelList);
		this.ruleList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.ruleList.setFont(new Font("Arial", Font.PLAIN, 12));
		rulesScrollPane.add(this.ruleList);
		rulesScrollPane.setViewportView(this.ruleList);

		JButton newRuleButton = new JButton("Crear");
		newRuleButton.setFont(new Font("Arial", Font.BOLD, 12));
		newRuleButton.setBounds(479, 11, 100, 30);
		newRuleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RuleListDialog.this.newRule();
			}
		});
		contentPanel.add(newRuleButton);

		JButton modifyRuleButton = new JButton("Editar");
		modifyRuleButton.setFont(new Font("Arial", Font.BOLD, 12));
		modifyRuleButton.setBounds(479, 53, 100, 30);
		modifyRuleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RuleListDialog.this.modifyRule();
			}
		});
		contentPanel.add(modifyRuleButton);

		JButton deleteRuleButton = new JButton("Eliminar");
		deleteRuleButton.setFont(new Font("Arial", Font.BOLD, 12));
		deleteRuleButton.setBounds(479, 95, 100, 30);
		contentPanel.add(deleteRuleButton);
		deleteRuleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RuleListDialog.this.deleteRule();
			}
		});

		JButton backButton = new JButton("Volver");
		backButton.setFont(new Font("Arial", Font.BOLD, 12));
		backButton.setBounds(479, 321, 100, 30);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RuleListDialog.this.dispose();
			}
		});
		contentPanel.add(backButton);
	}

	/**
	 * La funci�n encargada de recuperar todas las reglas que tenemos dentro del sistema y cargarlas dentro de la lista de reglas.
	 */
	private void loadRules() {
		this.ruleModelList.clear();
		try {
			for (Rule r : this.ruleService.findAll()) {
				this.ruleModelList.addElement(r);
			}
		} catch (CheckedException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * La funci�n para dar de alta una nueva regla.
	 */
	private void newRule() {
		RuleFormDialog dialog = this.ruleFormDialog.createNewDialog();
		dialog.setLocationRelativeTo(this);
		dialog.setModal(true);
		dialog.setVisible(true);
		this.loadRules();
	}

	/**
	 * La funci�n para modificar una regla.
	 */
	private void modifyRule() {
		if (this.ruleList.getSelectedIndex() != -1) {
			Rule rule = this.ruleList.getSelectedValue();
			RuleFormDialog dialog = this.ruleFormDialog.createEditDialog(rule);
			dialog.setLocationRelativeTo(this);
			dialog.setModal(true);
			dialog.setVisible(true);
			this.loadRules();
		}
	}

	/**
	 * La funci�n para eliminar una regla.
	 */
	private void deleteRule() {
		if (this.ruleList.getSelectedIndex() != -1) {
			Rule rule = this.ruleList.getSelectedValue();
			if (JOptionPane.showConfirmDialog(this, "Est� seguro de borrar la regla \"" + rule.getDescription() + "\"?", "Confirmaci�n",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				try {
					this.ruleService.delete(rule);
					this.loadRules();
				} catch (CheckedException ex) {
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * La funci�n encargada de inicializar la ventana para desplegarse.
	 * 
	 * @return La ventana para desplegar el listado de las reglas.
	 */
	public RuleListDialog createDialog() {
		this.setTitle("Listado de Reglas");
		this.loadRules();
		return this;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {

			UIManager.setLookAndFeel(new NimbusLookAndFeel());
			String[] files =
				{ "/com/proyecto/spring/general-application-context.xml" };
			HolderApplicationContext.initApplicationContext(files);

			RuleListDialog dialog = HolderApplicationContext.getContext().getBean(RuleListDialog.class).createDialog();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
