//
//public class CustomCell {
//    public static void main( String [] args ) { 
//        Object [] columnNames = new Object[]{ "Id", "Quantity" };
//        Object [][] data        = new Object[][]{ {"06", 1}, {"08", 2} };
//
//        JTable table = new JTable( data, columnNames ) { 
//            public TableCellRenderer getCellRenderer( int row, int column ) {
//                return new PlusMinusCellRenderer();
//            }
//         };
//
//        table.setRowHeight( 32 );
//        showFrame( table );
//    }
//
//    private static void showFrame( JTable table ) {
//        JFrame f = new JFrame("Custom Cell Renderer sample" );
//        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//        f.add( new JScrollPane( table ) );
//        f.pack();
//        f.setVisible( true );
//    }
//}
//
//class PlusMinusCellRenderer extends JPanel implements TableCellRenderer {
//        public Component getTableCellRendererComponent(
//                            final JTable table, Object value,
//                            boolean isSelected, boolean hasFocus,
//                            int row, int column) {
//                this.add( new JTextField( value.toString()  ) );
//                this.add( new JButton("+"));
//                this.add( new JButton("-"));
//                return this;
//        }
//}
//
//
//
//
//
//class ButtonsPanel extends JPanel {
//  public final List<JButton> buttons = new ArrayList<>();
//  public ButtonsPanel() {
//    super(new FlowLayout(FlowLayout.LEFT));
//    setOpaque(true);
//    for (Actions a : Actions.values()) {
//      JButton b = new JButton(a.toString());
//      b.setFocusable(false);
//      b.setRolloverEnabled(false);
//      add(b);
//      buttons.add(b);
//    }
//  }
//  protected void updateButtons(Object value) {
//    if (value instanceof EnumSet) {
//      EnumSet ea = (EnumSet) value;
//      removeAll();
//      if (ea.contains(Actions.PRINT)) {
//        add(buttons.get(0));
//      }
//      if (ea.contains(Actions.EDIT)) {
//        add(buttons.get(1));
//      }
//    }
//  }
//}
//
//class ButtonsRenderer implements TableCellRenderer {
//  private final ButtonsPanel panel = new ButtonsPanel();
//  @Override public Component getTableCellRendererComponent(
//      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//    panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
//    panel.updateButtons(value);
//    return panel;
//  }
//}
//
//class PrintAction extends AbstractAction {
//  private final JTable table;
//  public PrintAction(JTable table) {
//    super(Actions.PRINT.toString());
//    this.table = table;
//  }
//  @Override public void actionPerformed(ActionEvent e) {
//    JOptionPane.showMessageDialog(table, "Printing");
//  }
//}
//
//class EditAction extends AbstractAction {
//  private final JTable table;
//  public EditAction(JTable table) {
//    super(Actions.EDIT.toString());
//    this.table = table;
//  }
//  @Override public void actionPerformed(ActionEvent e) {
//    int row = table.convertRowIndexToModel(table.getEditingRow());
//    Object o = table.getModel().getValueAt(row, 0);
//    JOptionPane.showMessageDialog(table, "Editing: " + o);
//  }
//}
//
//class ButtonsEditor extends AbstractCellEditor implements TableCellEditor {
//  private final ButtonsPanel panel = new ButtonsPanel();
//  private final JTable table;
//  private Object o;
//  private class EditingStopHandler extends MouseAdapter implements ActionListener {
//    @Override public void mousePressed(MouseEvent e) {
//      Object o = e.getSource();
//      if (o instanceof TableCellEditor) {
//        actionPerformed(null);
//      } else if (o instanceof JButton) {
//        ButtonModel m = ((JButton) e.getComponent()).getModel();
//        if (m.isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) {
//          panel.setBackground(table.getBackground());
//        }
//      }
//    }
//    @Override public void actionPerformed(ActionEvent e) {
//      EventQueue.invokeLater(new Runnable() {
//        @Override public void run() {
//          fireEditingStopped();
//        }
//      });
//    }
//  }
//  public ButtonsEditor(JTable table) {
//    super();
//    this.table = table;
//    panel.buttons.get(0).setAction(new PrintAction(table));
//    panel.buttons.get(1).setAction(new EditAction(table));
//
//    EditingStopHandler handler = new EditingStopHandler();
//    for (JButton b : panel.buttons) {
//      b.addMouseListener(handler);
//      b.addActionListener(handler);
//    }
//    panel.addMouseListener(handler);
//  }
//  @Override public Component getTableCellEditorComponent(
//      JTable table, Object value, boolean isSelected, int row, int column) {
//    panel.setBackground(table.getSelectionBackground());
//    panel.updateButtons(value);
//    o = value;
//    return panel;
//  }
//  @Override public Object getCellEditorValue() {
//    return o;
//  }
//}